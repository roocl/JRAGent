package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.chatSession.CreateChatSessionRequest;
import org.jragent.model.dto.chatSession.UpdateChatSessionRequest;
import org.jragent.model.vo.chatSession.CreateChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionsResponse;
import org.jragent.service.ChatSessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "聊天会话", description = "聊天会话接口")
public class ChatSessionController {
    private final ChatSessionService chatSessionService;

    @GetMapping("/chat-sessions/{chatSessionId}")
    @Operation(summary = "根据会话ID查询聊天会话")
    public ApiResponse<GetChatSessionResponse> getChatSession(@PathVariable String chatSessionId) {
        return ApiResponse.success(chatSessionService.getChatSession(chatSessionId));
    }

    @GetMapping("/chat-sessions/agent/{agentId}")
    @Operation(summary = "根据智能体ID查询聊天会话")
    public ApiResponse<GetChatSessionsResponse> getChatSessionsByAgentId(@PathVariable String agentId) {
        return ApiResponse.success(chatSessionService.getChatSessionsByAgentId(agentId));
    }

    @GetMapping("/chat-sessions")
    @Operation(summary = "查询所有聊天会话")
    public ApiResponse<GetChatSessionsResponse> getChatSessions() {
        return ApiResponse.success(chatSessionService.getChatSessions());
    }

    @PostMapping("/chat-sessions")
    @Operation(summary = "创建聊天会话")
    public ApiResponse<CreateChatSessionResponse> createChatSession(@Valid @RequestBody CreateChatSessionRequest request) {
        return ApiResponse.success(chatSessionService.createChatSession(request));
    }

    @PatchMapping("/chat-sessions/{chatSessionId}")
    @Operation(summary = "更新聊天会话")
    public ApiResponse<Void> updateChatSession(
            @Parameter(description = "聊天会话ID", required = true) @PathVariable String chatSessionId,
            @Valid @RequestBody UpdateChatSessionRequest request
    ) {
        chatSessionService.updateChatSession(chatSessionId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/chat-sessions/{chatSessionId}")
    @Operation(summary = "删除聊天会话")
    public ApiResponse<Void> deleteChatSession(@PathVariable String chatSessionId) {
        chatSessionService.deleteChatSession(chatSessionId);
        return ApiResponse.success();
    }
}
