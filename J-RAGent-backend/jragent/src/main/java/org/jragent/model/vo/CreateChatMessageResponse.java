package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateChatMessageResponse", description = "创建消息响应")
public class CreateChatMessageResponse {
    @Schema(description = "生成的消息ID", example = "msg_001")
    private String chatMessageId;
}
