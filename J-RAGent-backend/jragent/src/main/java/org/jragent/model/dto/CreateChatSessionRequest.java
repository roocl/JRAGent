package org.jragent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CreateChatSessionRequest", description = "创建会话请求")
public class CreateChatSessionRequest {
    @Schema(description = "智能体ID", example = "agent_001")
    private String agentId;

    @Schema(description = "会话标题", example = "东京五日游规划")
    private String title;
}

