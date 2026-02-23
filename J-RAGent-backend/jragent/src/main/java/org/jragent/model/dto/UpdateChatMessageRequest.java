package org.jragent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UpdateChatMessageRequest", description = "更新消息请求")
public class UpdateChatMessageRequest {
    @Schema(description = "消息内容", example = "请改成三日游方案")
    private String content;

    @Schema(description = "消息附加元数据")
    private ChatMessageDTO.MetaData metadata;
}
