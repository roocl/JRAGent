package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "GetChatSessionResponse", description = "获取单个会话响应")
public class GetChatSessionResponse {
    @Schema(description = "聊天会话信息")
    private ChatSessionVO chatSession;
}
