package org.jragent.model.vo.chatSession;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetChatSessionsResponse", description = "获取会话列表响应")
public class GetChatSessionsResponse {
    @Schema(description = "聊天会话列表")
    private List<ChatSessionVO> chatSessions;
}
