package org.jragent.model.dto.chatMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateChatMessageRequest", description = "创建消息请求")
public class CreateChatMessageRequest {
    @NotBlank(message = "智能体ID不能为空")
    @Schema(description = "智能体ID", example = "agent_001")
    private String agentId;

    @NotBlank(message = "会话ID不能为空")
    @Schema(description = "会话ID", example = "session_001")
    private String sessionId;

    @NotNull(message = "消息角色不能为空")
    @Schema(description = "消息角色", example = "USER")
    private ChatMessageDTO.RoleType role;

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容", example = "请帮我规划一次周末短途旅行")
    private String content;

    @Schema(description = "消息附加元数据")
    private ChatMessageDTO.MetaData metadata;
}
