package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetAgentsResponse", description = "获取智能体响应")
public class GetAgentsResponse {
    // 不能只获取单个agent，所以要多一层封装
    private List<AgentVO> agents;
}
