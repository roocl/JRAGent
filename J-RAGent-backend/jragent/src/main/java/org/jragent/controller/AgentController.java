package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.dto.UpdateAgentRequest;
import org.jragent.model.vo.CreateAgentResponse;
import org.jragent.model.vo.GetAgentsResponse;
import org.jragent.service.AgentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "智能体", description = "智能体接口")
public class AgentController {
    private final AgentService agentService;

    @GetMapping("/agents")
    @Operation(summary = "查询智能体列表")
    public ApiResponse<GetAgentsResponse> getAgents() {
        return ApiResponse.success(agentService.getAgents());
    }

    @PostMapping("/agents")
    @Operation(summary = "新建智能体")
    public ApiResponse<CreateAgentResponse> createAgent(@RequestBody CreateAgentRequest request) {
        return ApiResponse.success(agentService.createAgent(request));
    }

    @PatchMapping("/agents/{agentId}")
    @Operation(summary = "更新智能体")
    public ApiResponse<Void> updateAgent(@PathVariable String agentId, @RequestBody UpdateAgentRequest request) {
        agentService.updateAgent(agentId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/agents/{agentId}")
    @Operation(summary = "删除智能体")
    public ApiResponse<Void> deleteAgent(@PathVariable String agentId) {
        agentService.deleteAgent(agentId);
        return ApiResponse.success();
    }
}
