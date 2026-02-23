package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.CreateChatMessageRequest;
import org.jragent.model.dto.UpdateChatMessageRequest;
import org.jragent.model.vo.CreateChatMessageResponse;
import org.jragent.service.ChatMessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "聊天消息", description = "聊天消息接口")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/chat-messages")
    @Operation(summary = "创建聊天消息")
    public ApiResponse<CreateChatMessageResponse> createChatMessage(@RequestBody CreateChatMessageRequest request) {
        return ApiResponse.success(chatMessageService.createChatMessage(request));
    }

    @PatchMapping("/chat-messages/{chatMessageId}")
    @Operation(summary = "更新聊天消息")
    public ApiResponse<Void> updateChatMessage(
            @Parameter(description = "聊天消息ID", required = true) @PathVariable String chatMessageId,
            @RequestBody UpdateChatMessageRequest request
    ) {
        chatMessageService.updateChatMessage(chatMessageId, request);
        return ApiResponse.success();
    }
}
