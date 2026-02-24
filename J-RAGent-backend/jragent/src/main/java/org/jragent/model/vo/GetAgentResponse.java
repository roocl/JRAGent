package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "GetAgentResponse", description = "获取单个智能体响应")
public class GetAgentResponse {
    @Schema(description = "智能体信息")
    private AgentVO agent;
}
