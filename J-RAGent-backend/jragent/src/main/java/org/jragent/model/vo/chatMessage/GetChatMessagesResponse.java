package org.jragent.model.vo.chatMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetChatMessagesResponse", description = "获取会话消息列表响应")
public class GetChatMessagesResponse {
    @Schema(description = "聊天消息列表")
    private List<ChatMessageVO> chatMessages;
}
