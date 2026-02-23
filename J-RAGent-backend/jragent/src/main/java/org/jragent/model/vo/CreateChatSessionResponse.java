package org.jragent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateChatSessionResponse", description = "创建会话响应")
public class CreateChatSessionResponse {
    @Schema(description = "生成的会话ID", example = "session_001")
    private String chatSessionId;
}
