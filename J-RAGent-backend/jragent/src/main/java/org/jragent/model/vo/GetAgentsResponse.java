package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetAgentsResponse", description = "获取智能体响应")
public class GetAgentsResponse {
    @Schema(description = "智能体列表")
    private List<AgentVO> agents;
}
