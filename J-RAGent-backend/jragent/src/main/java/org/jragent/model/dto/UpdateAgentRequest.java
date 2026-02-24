package org.jragent.model.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "UpdateAgentRequest", description = "更新智能体请求")
public class UpdateAgentRequest {
    @Schema(description = "智能体名称", example = "旅游助手")
    private String name;

    @Schema(description = "智能体描述", example = "帮助用户制定旅游计划")
    private String description;

    @Schema(description = "系统提示词", example = "你是一个专业的旅行规划助手。")
    private String systemPrompt;

    @Schema(description = "模型名称", example = "deepseek-chat")
    private String model;

    @ArraySchema(
            schema = @Schema(description = "工具名称", example = "WeatherTestTool"),
            arraySchema = @Schema(description = "允许启用的可选工具名称列表")
    )
    private List<String> allowedTools;

//    private List<String> allowedKbs;

    @Schema(description = "聊天参数配置")
    private AgentDTO.ChatOptions chatOptions;
}
