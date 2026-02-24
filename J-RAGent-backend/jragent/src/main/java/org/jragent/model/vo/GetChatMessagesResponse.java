package org.jragent.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChatMessagesResponse {
    private List<ChatMessageVO> chatMessages;
}
