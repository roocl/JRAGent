package org.jragent.agent;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToolAwareChatMemory implements ChatMemory {

    private final Map<String, List<Message>> store = new ConcurrentHashMap<>();
    private final int maxMessages;

    public ToolAwareChatMemory(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    @Override
    public void add(@NonNull String conversationId, @NonNull List<Message> messages) {
        store.computeIfAbsent(conversationId, k -> new ArrayList<>()).addAll(messages);
    }

    @Override
    public @NonNull List<Message> get(@NonNull String conversationId) {
        List<Message> messages = store.getOrDefault(conversationId, List.of());
        if (messages.size() <= maxMessages) {
            return new ArrayList<>(messages);
        }

        // 从尾部保留maxMessages条消息
        int startIndex = messages.size() - maxMessages;

        // 如果截断点在assistant(tool_calls)和tool中间，则跳过孤立的ToolResponseMessage，确保消息序列合法
        while (startIndex < messages.size() && messages.get(startIndex) instanceof ToolResponseMessage) {
            startIndex++;
        }

        return new ArrayList<>(messages.subList(startIndex, messages.size()));
    }

    @Override
    public void clear(@NonNull String conversationId) {
        store.remove(conversationId);
    }
}
