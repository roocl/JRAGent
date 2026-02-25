package org.jragent.model.dto.chatMessage;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(name = "ChatMessageDTO", description = "消息数据传输对象")
public class ChatMessageDTO {
    @Schema(description = "消息ID", example = "msg_001")
    private String id;

    @Schema(description = "会话ID", example = "session_001")
    private String sessionId;

    @Schema(description = "消息角色")
    private RoleType role;

    @Schema(description = "消息内容", example = "请帮我生成旅游计划")
    private String content;

    @Schema(description = "消息元数据")
    private MetaData metadata;

    @Schema(description = "创建时间", example = "2026-02-23T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2026-02-23T10:00:00")
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @Schema(name = "ChatMessageMetaData", description = "消息元数据")
    public static class MetaData {
        @Schema(description = "工具执行返回信息")
        private ToolResponseMessage.ToolResponse toolResponse;
        @Schema(description = "模型返回的工具调用列表")
        private List<AssistantMessage.ToolCall> toolCalls;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "消息角色枚举")
    public enum RoleType {
        @Schema(description = "用户消息")
        USER("user"),
        @Schema(description = "助手消息")
        ASSISTANT("assistant"),
        @Schema(description = "系统消息")
        SYSTEM("system"),
        @Schema(description = "工具消息")
        TOOL("tool");

        @JsonValue
        private final String role;

        public static RoleType fromRole(String role) {
            for (RoleType value : values()) {
                if (value.role.equals(role)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
