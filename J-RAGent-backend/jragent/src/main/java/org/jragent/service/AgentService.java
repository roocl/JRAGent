package org.jragent.service;

import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.vo.CreateAgentResponse;

public interface AgentService {
    CreateAgentResponse createAgent(CreateAgentRequest request);
}
