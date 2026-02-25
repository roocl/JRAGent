package org.jragent.service;

import org.jragent.model.dto.chatMessage.ChatMessageDTO;
import org.jragent.model.dto.chatMessage.CreateChatMessageRequest;
import org.jragent.model.dto.chatMessage.UpdateChatMessageRequest;
import org.jragent.model.vo.chatMessage.CreateChatMessageResponse;
import org.jragent.model.vo.chatMessage.GetChatMessagesResponse;

import java.util.List;

public interface ChatMessageService {

    GetChatMessagesResponse getChatMessagesBySessionId(String sessionId);

    List<ChatMessageDTO> getChatMessagesBySessionIdRecently(String sessionId, int limit);

    CreateChatMessageResponse createChatMessage(CreateChatMessageRequest request);

    CreateChatMessageResponse createChatMessage(ChatMessageDTO chatMessageDTO);

    void updateChatMessage(String chatMessageId, UpdateChatMessageRequest request);

    void deleteChatMessage(String chatMessageId);
}
