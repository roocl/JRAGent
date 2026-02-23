package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jragent.agent.tools.Tool;
import org.jragent.model.common.ApiResponse;
import org.jragent.service.ToolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "工具", description = "工具接口")
public class ToolController {

    private final ToolService toolService;

    @GetMapping("/tools")
    @Operation(summary = "可选工具列表")
    public ApiResponse<List<Tool>> getOptionalTools() {
        return ApiResponse.success(toolService.getOptionalTools());
    }
}
