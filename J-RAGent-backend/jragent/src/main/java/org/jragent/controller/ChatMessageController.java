package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.chatMessage.CreateChatMessageRequest;
import org.jragent.model.dto.chatMessage.UpdateChatMessageRequest;
import org.jragent.model.vo.chatMessage.CreateChatMessageResponse;
import org.jragent.model.vo.chatMessage.GetChatMessagesResponse;
import org.jragent.service.ChatMessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "聊天消息", description = "聊天消息接口")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/chat-messages/session/{sessionId}")
    @Operation(summary = "根据会话ID查询聊天消息")
    public ApiResponse<GetChatMessagesResponse> getChatMessagesBySessionId(@PathVariable String sessionId) {
        return ApiResponse.success(chatMessageService.getChatMessagesBySessionId(sessionId));
    }

    @PostMapping("/chat-messages")
    @Operation(summary = "创建聊天消息")
    public ApiResponse<CreateChatMessageResponse> createChatMessage(@Valid @RequestBody CreateChatMessageRequest request) {
        return ApiResponse.success(chatMessageService.createChatMessage(request));
    }

    @PatchMapping("/chat-messages/{chatMessageId}")
    @Operation(summary = "更新聊天消息")
    public ApiResponse<Void> updateChatMessage(
            @Parameter(description = "聊天消息ID", required = true) @PathVariable String chatMessageId,
            @Valid @RequestBody UpdateChatMessageRequest request
    ) {
        chatMessageService.updateChatMessage(chatMessageId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/chat-messages/{chatMessageId}")
    @Operation(summary = "删除聊天消息")
    public ApiResponse<Void> deleteChatMessage(@PathVariable String chatMessageId) {
        chatMessageService.deleteChatMessage(chatMessageId);
        return ApiResponse.success();
    }
}
