package org.jragent.model.vo.chatSession;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "ChatSessionVO", description = "会话展示对象")
public class ChatSessionVO {
    private String id;

    private String agentId;

    private String title;
}
