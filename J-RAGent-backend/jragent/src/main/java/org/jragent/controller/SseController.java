package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@AllArgsConstructor
@Tag(name = "SSE", description = "SSE接口")
public class SseController {
    private SseService sseService;

    // 处理 sse 连接
    @GetMapping(value = "/connect/{chatSessionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE连接")
    public SseEmitter connect(@Parameter(description = "聊天会话ID", required = true) @PathVariable String chatSessionId) {
        return sseService.connect(chatSessionId);
    }
}
