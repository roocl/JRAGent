package org.jragent.service;

import org.jragent.model.dto.ChatMessageDTO;
import org.jragent.model.dto.CreateChatMessageRequest;
import org.jragent.model.dto.UpdateChatMessageRequest;
import org.jragent.model.vo.CreateChatMessageResponse;
import org.jragent.model.vo.GetChatMessagesResponse;

import java.util.List;

public interface ChatMessageService {

    GetChatMessagesResponse getChatMessagesBySessionId(String sessionId);

    List<ChatMessageDTO> getChatMessagesBySessionIdRecently(String sessionId, int limit);

    CreateChatMessageResponse createChatMessage(CreateChatMessageRequest request);

    CreateChatMessageResponse createChatMessage(ChatMessageDTO chatMessageDTO);

    void updateChatMessage(String chatMessageId, UpdateChatMessageRequest request);

    void deleteChatMessage(String chatMessageId);
}
