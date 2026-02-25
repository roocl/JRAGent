package org.jragent.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jragent.model.vo.chatMessage.ChatMessageVO;

@Data
@AllArgsConstructor
@Builder
@Schema(name = "SseMessage", description = "SSE推送消息体")
public class SseMessage {

    @Schema(description = "消息类型")
    private Type type;
    @Schema(description = "消息负载")
    private Payload payload;
    @Schema(description = "消息元数据")
    private Metadata metadata;

    @Data
    @AllArgsConstructor
    @Builder
    @Schema(name = "SsePayload", description = "SSE消息负载")
    public static class Payload {
        @Schema(description = "聊天消息内容")
        private ChatMessageVO message;
        @Schema(description = "状态文本", example = "正在思考")
        private String statusText;
        @Schema(description = "是否完成", example = "false")
        private Boolean done;
    }

    @Data
    @AllArgsConstructor
    @Builder
    @Schema(name = "SseMetadata", description = "SSE消息元数据")
    public static class Metadata {
        @Schema(description = "聊天消息ID", example = "msg_001")
        private String chatMessageId;
    }

    @Schema(description = "SSE消息类型")
    public enum Type {
        // AI生成
        AI_GENERATED_CONTENT,
        // AI规划
        AI_PLANNING,
        // AI思考
        AI_THINKING,
        // AI执行
        AI_EXECUTING,
        // AI完成
        AI_DONE,
    }
}
