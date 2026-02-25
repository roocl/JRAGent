package org.jragent.service;

import org.jragent.model.dto.agent.CreateAgentRequest;
import org.jragent.model.dto.agent.UpdateAgentRequest;
import org.jragent.model.vo.agent.CreateAgentResponse;
import org.jragent.model.vo.agent.GetAgentResponse;
import org.jragent.model.vo.agent.GetAgentsResponse;

public interface AgentService {
    GetAgentResponse getAgent(String agentId);

    GetAgentsResponse getAgents();

    CreateAgentResponse createAgent(CreateAgentRequest request);

    void updateAgent(String agentId, UpdateAgentRequest request);

    void deleteAgent(String agentId);
}
