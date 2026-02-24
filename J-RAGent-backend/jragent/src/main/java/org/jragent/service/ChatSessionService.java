package org.jragent.service;

import org.jragent.model.dto.CreateChatSessionRequest;
import org.jragent.model.dto.UpdateChatSessionRequest;
import org.jragent.model.vo.CreateChatSessionResponse;
import org.jragent.model.vo.GetChatSessionResponse;
import org.jragent.model.vo.GetChatSessionsResponse;

public interface ChatSessionService {
    GetChatSessionResponse getChatSession(String chatSessionId);

    GetChatSessionsResponse getChatSessionsByAgentId(String agentId);

    GetChatSessionsResponse getChatSessions();

    CreateChatSessionResponse createChatSession(CreateChatSessionRequest request);

    void updateChatSession(String chatSessionId, UpdateChatSessionRequest request);

    void deleteChatSession(String chatSessionId);
}
