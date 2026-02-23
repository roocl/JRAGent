package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.CreateChatSessionRequest;
import org.jragent.model.dto.UpdateChatSessionRequest;
import org.jragent.model.vo.CreateChatSessionResponse;
import org.jragent.service.ChatSessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "聊天会话", description = "聊天会话接口")
public class ChatSessionController {
    private final ChatSessionService chatSessionService;

    @PostMapping("/chat-sessions")
    @Operation(summary = "创建聊天会话")
    public ApiResponse<CreateChatSessionResponse> createChatSession(@RequestBody CreateChatSessionRequest request) {
        return ApiResponse.success(chatSessionService.createChatSession(request));
    }

    @PatchMapping("/chat-sessions/{chatSessionId}")
    @Operation(summary = "更新聊天会话")
    public ApiResponse<Void> updateChatSession(
            @Parameter(description = "聊天会话ID", required = true) @PathVariable String chatSessionId,
            @RequestBody UpdateChatSessionRequest request
    ) {
        chatSessionService.updateChatSession(chatSessionId, request);
        return ApiResponse.success();
    }
}
