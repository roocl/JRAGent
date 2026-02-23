package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.vo.CreateAgentResponse;
import org.jragent.service.AgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "Agent", description = "Agent接口")
public class AgentController {
    private final AgentService agentService;

    @PostMapping("/agents")
    @Operation(summary = "新建Agent")
    public ApiResponse<CreateAgentResponse> createAgent(@RequestBody CreateAgentRequest request) {
        return ApiResponse.success(agentService.createAgent(request));
    }
}
