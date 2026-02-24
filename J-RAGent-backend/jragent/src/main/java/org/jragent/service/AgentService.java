package org.jragent.service;

import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.dto.UpdateAgentRequest;
import org.jragent.model.vo.CreateAgentResponse;
import org.jragent.model.vo.GetAgentsResponse;

public interface AgentService {
    GetAgentsResponse getAgents();

    CreateAgentResponse createAgent(CreateAgentRequest request);

    void updateAgent(String agentId, UpdateAgentRequest request);

    void deleteAgent(String agentId);
}
