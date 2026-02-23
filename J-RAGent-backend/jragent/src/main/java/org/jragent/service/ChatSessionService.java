package org.jragent.service;

import org.jragent.model.dto.CreateChatSessionRequest;
import org.jragent.model.dto.UpdateChatSessionRequest;
import org.jragent.model.vo.CreateChatSessionResponse;

public interface ChatSessionService {
    CreateChatSessionResponse createChatSession(CreateChatSessionRequest request);

    void updateChatSession(String chatSessionId, UpdateChatSessionRequest request);
}
