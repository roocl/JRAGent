package org.jragent.service;

import org.jragent.model.dto.ChatMessageDTO;
import org.jragent.model.dto.CreateChatMessageRequest;
import org.jragent.model.dto.UpdateChatMessageRequest;
import org.jragent.model.vo.CreateChatMessageResponse;

import java.util.List;

public interface ChatMessageService {

    CreateChatMessageResponse createChatMessage(CreateChatMessageRequest request);

    CreateChatMessageResponse createChatMessage(ChatMessageDTO chatMessageDTO);

    void updateChatMessage(String chatMessageId, UpdateChatMessageRequest request);

    List<ChatMessageDTO> getChatMessagesBySessionIdRecently(String sessionId, int limit);

}
