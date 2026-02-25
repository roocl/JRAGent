package org.jragent.model.dto.chatSession;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "CreateChatSessionRequest", description = "创建会话请求")
public class CreateChatSessionRequest {
    @NotBlank(message = "智能体ID不能为空")
    @Schema(description = "智能体ID", example = "agent_001")
    private String agentId;

    @NotBlank(message = "会话标题不能为空")
    @Schema(description = "会话标题", example = "东京五日游规划")
    private String title;
}
