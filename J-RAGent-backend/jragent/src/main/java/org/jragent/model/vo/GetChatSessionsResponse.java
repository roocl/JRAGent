package org.jragent.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChatSessionsResponse {
    private List<ChatSessionVO> chatSessions;
}
