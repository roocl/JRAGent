package org.jragent.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(name = "AgentDTO", description = "智能体数据传输对象")
public class AgentDTO {
    @Schema(description = "智能体ID", example = "agent_001")
    private String id;

    @Schema(description = "智能体名称", example = "旅游助手")
    private String name;

    @Schema(description = "智能体描述", example = "帮助用户制定旅行计划")
    private String description;

    @Schema(description = "系统提示词", example = "你是一个专业的旅行规划助手。")
    private String systemPrompt;

    @Schema(description = "模型类型")
    private ModelType model;

    @Schema(description = "允许启用的可选工具名称列表")
    private List<String> allowedTools;

//    private List<String> allowedKbs;

    @Schema(description = "聊天参数配置")
    private ChatOptions chatOptions;

    @Schema(description = "创建时间", example = "2026-02-23T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2026-02-23T10:00:00")
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    @Schema(description = "模型类型枚举")
    public enum ModelType {
        @Schema(description = "DeepSeek Chat 模型")
        DEEPSEEK_CHAT("deepseek-chat"),
        @Schema(description = "智谱 GLM-4.7 模型")
        GLM_4_7("glm-4.7");

        @JsonValue
        private final String modelName;

        public static ModelType fromModelName(String modelName) {
            for (ModelType type : ModelType.values()) {
                if (type.modelName.equals(modelName)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown model type: " + modelName);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name = "AgentChatOptions", description = "智能体聊天参数")
    public static class ChatOptions {
        // 温度
        @Schema(description = "温度参数，值越大随机性越高", example = "0.7")
        private Double temperature;
        // 候选词范围
        @Schema(description = "Top-P 采样参数", example = "1.0")
        private Double topP;
        // 聊天消息窗口长度
        @Schema(description = "历史消息窗口长度", example = "10")
        private Integer messageLength;

        private static final Double DEFAULT_TEMPERATURE = 0.7;
        private static final Double DEFAULT_TOP_P = 1.0;
        private static final Integer DEFAULT_MESSAGE_LENGTH = 10;

        public static ChatOptions defaultOptions() {
            return ChatOptions.builder()
                    .temperature(DEFAULT_TEMPERATURE)
                    .topP(DEFAULT_TOP_P)
                    .messageLength(DEFAULT_MESSAGE_LENGTH)
                    .build();
        }
    }
}
