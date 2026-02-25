package org.jragent.model.dto.chatSession;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "ChatSessionDTO", description = "会话数据传输对象")
public class ChatSessionDTO {
    @Schema(description = "会话ID", example = "session_001")
    private String id;

    @Schema(description = "智能体ID", example = "agent_001")
    private String agentId;

    @Schema(description = "会话标题", example = "东京五日游规划")
    private String title;

    @Schema(description = "会话元数据")
    private MetaData metadata;

    @Schema(description = "创建时间", example = "2026-02-23T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2026-02-23T10:00:00")
    private LocalDateTime updatedAt;

    @Data
    @Schema(name = "ChatSessionMetaData", description = "会话元数据")
    public static class MetaData {
    }
}
