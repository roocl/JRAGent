package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.jragent.model.dto.ChatMessageDTO;

@Data
@Builder
@Schema(name = "ChatMessageVO", description = "消息展示对象")
public class ChatMessageVO {
    @Schema(description = "消息ID", example = "msg_001")
    private String id;

    @Schema(description = "会话ID", example = "session_001")
    private String sessionId;

    @Schema(description = "消息角色")
    private ChatMessageDTO.RoleType role;

    @Schema(description = "消息内容", example = "这是助手生成的回复")
    private String content;

    @Schema(description = "消息元数据")
    private ChatMessageDTO.MetaData metadata;
}
