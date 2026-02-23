package org.jragent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UpdateChatSessionRequest", description = "更新会话请求")
public class UpdateChatSessionRequest {
    @Schema(description = "会话标题", example = "已更新：东京五日游规划")
    private String title;
}
