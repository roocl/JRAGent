package org.jragent.model.dto.agent;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "CreateAgentRequest", description = "创建智能体请求")
public class CreateAgentRequest {
    @NotBlank(message = "智能体名称不能为空")
    @Schema(description = "智能体名称", example = "旅游助手")
    private String name;

    @NotBlank(message = "智能体描述不能为空")
    @Schema(description = "智能体描述", example = "帮助用户制定旅游计划")
    private String description;

    @NotBlank(message = "系统提示词不能为空")
    @Schema(description = "系统提示词", example = "你是一个专业的旅行规划助手。")
    private String systemPrompt;

    @NotBlank(message = "模型名称不能为空")
    @Schema(description = "模型名称", example = "deepseek-chat")
    private String model;

    @ArraySchema(
            schema = @Schema(description = "工具名称", example = "WeatherTestTool"),
            arraySchema = @Schema(description = "允许启用的可选工具名称列表")
    )
    private List<String> allowedTools;
//    private List<String> allowedKbs;

    @Valid
    @NotNull(message = "聊天参数配置不能为空")
    @Schema(description = "聊天参数配置")
    private AgentDTO.ChatOptions chatOptions;
}
