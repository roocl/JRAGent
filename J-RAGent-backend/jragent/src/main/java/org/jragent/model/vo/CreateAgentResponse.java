package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateAgentResponse", description = "创建智能体响应")
public class CreateAgentResponse {
    @Schema(description = "生成的智能体ID", example = "agent_001")
    private String agentId;
}
