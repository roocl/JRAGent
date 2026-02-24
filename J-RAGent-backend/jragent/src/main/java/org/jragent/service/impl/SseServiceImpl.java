package org.jragent.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jragent.message.SseMessage;
import org.jragent.service.SseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor

public class SseServiceImpl implements SseService {
    // SSE超时时间
    private static final long SSE_TIMEOUT_MILLIS = 30 * 60 * 1000L;
    // 心跳间隔
    private static final long HEARTBEAT_INTERVAL_SECONDS = 25L;
    // 统一心跳线程池
    private static final ScheduledExecutorService HEARTBEAT_EXECUTOR = Executors.newScheduledThreadPool(1);


    private final ConcurrentMap<String, SseEmitter> clients = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;

    @Override
    public SseEmitter connect(String chatSessionId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MILLIS);

        SseEmitter oldEmitter = clients.put(chatSessionId, emitter);
        // 如果有旧连接则关闭，避免一个会话多个连接导致的推送混乱
        if (oldEmitter != null) {
            oldEmitter.complete();
        }

        //注册生命周期回调
        registerLifecycleCallbacks(chatSessionId, emitter);
        // 启动心跳任务
        startHeartbeat(chatSessionId, emitter);

        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("connected"));
        } catch (IOException e) {
            removeEmitter(chatSessionId, emitter);
            throw new RuntimeException("初始化SSE连接失败", e);
        }

        return emitter;
    }

    @Override
    public void send(String chatSessionId, SseMessage message) {
        SseEmitter emitter = clients.get(chatSessionId);
        // 无连接
        if (emitter == null) {
            log.debug("找不到chatSessionID的客户端: {}", chatSessionId);
            return;
        }

        try {
            String sseMessageStr = objectMapper.writeValueAsString(message);
            emitter.send(SseEmitter.event()
                    .name("message")
                    .data(sseMessageStr));
        } catch (Exception e) {
            removeEmitter(chatSessionId, emitter);
            log.warn("SSE推送失败, chatSessionId: {}, error: {}", chatSessionId, e.getMessage());
        }
    }

    private void registerLifecycleCallbacks(String chatSessionId, SseEmitter emitter) {
        emitter.onCompletion(() -> removeEmitter(chatSessionId, emitter));
        emitter.onTimeout(() -> removeEmitter(chatSessionId, emitter));
        emitter.onError(error -> removeEmitter(chatSessionId, emitter));
    }

    private void startHeartbeat(String chatSessionId, SseEmitter emitter) {
        // 从ConcurrentMap中移除
        ScheduledFuture<?> oldTask = heartbeatTasks.remove(chatSessionId);
        // 取消旧任务，避免同一会话存在多个心跳线程
        if (oldTask != null) {
            // 停止心跳任务
            oldTask.cancel(true);
        }

        ScheduledFuture<?> heartbeatTask = HEARTBEAT_EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                SseEmitter current = clients.get(chatSessionId);
                // 如果当前正在运行的心跳任务对应的是旧连接，就立即停止该定时任务并退出
                if (current != emitter) {
                    cancelHeartbeat(chatSessionId);
                    return;
                }
                // 发送信号以维系连接
                emitter.send(SseEmitter.event().name("ping").data("heartbeat"));
            } catch (Exception e) {
                removeEmitter(chatSessionId, emitter);
            }
        }, HEARTBEAT_INTERVAL_SECONDS, HEARTBEAT_INTERVAL_SECONDS, TimeUnit.SECONDS);

        heartbeatTasks.put(chatSessionId, heartbeatTask);
    }

    // 只有ConcurrentMap中仍是当前连接时才移除，避免误删新连接
    private void removeEmitter(String chatSessionId, SseEmitter emitter) {
        // 如果key k(chatSessionId)对应的value existing就是当前连接，则移除，否则维持原样
        clients.compute(chatSessionId, (k, existing) -> existing == emitter ? null : existing);
        cancelHeartbeat(chatSessionId);
    }

    // 取消并清理会话心跳任务
    private void cancelHeartbeat(String chatSessionId) {
        ScheduledFuture<?> task = heartbeatTasks.remove(chatSessionId);
        if (task != null) {
            task.cancel(true);
        }
    }
}
