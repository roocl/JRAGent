package org.jragent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateChatMessageRequest", description = "创建消息请求")
public class CreateChatMessageRequest {
    @Schema(description = "智能体ID", example = "agent_001")
    private String agentId;

    @Schema(description = "会话ID", example = "session_001")
    private String sessionId;

    @Schema(description = "消息角色", example = "USER")
    private ChatMessageDTO.RoleType role;

    @Schema(description = "消息内容", example = "请帮我规划一次周末短途旅行")
    private String content;

    @Schema(description = "消息附加元数据")
    private ChatMessageDTO.MetaData metadata;
}
