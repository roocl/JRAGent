package org.jragent.service;

import org.jragent.model.dto.chatSession.CreateChatSessionRequest;
import org.jragent.model.dto.chatSession.UpdateChatSessionRequest;
import org.jragent.model.vo.chatSession.CreateChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionsResponse;

public interface ChatSessionService {
    GetChatSessionResponse getChatSession(String chatSessionId);

    GetChatSessionsResponse getChatSessionsByAgentId(String agentId);

    GetChatSessionsResponse getChatSessions();

    CreateChatSessionResponse createChatSession(CreateChatSessionRequest request);

    void updateChatSession(String chatSessionId, UpdateChatSessionRequest request);

    void deleteChatSession(String chatSessionId);
}
