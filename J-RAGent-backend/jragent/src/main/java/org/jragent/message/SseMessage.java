package org.jragent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jragent.model.vo.ChatMessageVO;

@Data
@AllArgsConstructor
@Builder
public class SseMessage {

    private Type type;
    private Payload payload;
    private Metadata metadata;

    @Data
    @AllArgsConstructor
    @Builder
    public static class Payload {
        private ChatMessageVO message;
        private String statusText;
        private Boolean done;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class Metadata {
        private String chatMessageId;
    }

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
