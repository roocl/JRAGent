package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.jragent.converter.AgentConverter;
import org.jragent.exception.BaseException;
import org.jragent.mapper.AgentMapper;
import org.jragent.model.dto.agent.CreateAgentRequest;
import org.jragent.model.dto.agent.UpdateAgentRequest;
import org.jragent.model.entity.Agent;
import org.jragent.model.dto.agent.AgentDTO;
import org.jragent.model.vo.agent.AgentVO;
import org.jragent.model.vo.agent.CreateAgentResponse;
import org.jragent.model.vo.agent.GetAgentResponse;
import org.jragent.model.vo.agent.GetAgentsResponse;
import org.jragent.service.AgentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentMapper agentMapper;

    private final AgentConverter agentConverter;

    @Override
    public GetAgentResponse getAgent(String agentId) {
        Agent agent = agentMapper.selectById(agentId);
        if (agent == null) {
            throw new BaseException("Agent不存在: " + agentId);
        }

        try {
            AgentVO agentVO = agentConverter.toVO(agent);
            return GetAgentResponse.builder()
                    .agent(agentVO)
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("查询agent时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public GetAgentsResponse getAgents() {
        List<Agent> agents = agentMapper.selectAll();

        List<AgentVO> agentVOS = agents.stream().map(agent -> {
            try {
                return agentConverter.toVO(agent);
            } catch (JsonProcessingException e) {
                throw new BaseException();
            }
        }).toList();

        return GetAgentsResponse.builder()
                .agents(agentVOS)
                .build();
    }

    @Override
    public CreateAgentResponse createAgent(CreateAgentRequest request) {
        try {
            AgentDTO agentDTO = agentConverter.toDTO(request);

            Agent agent = agentConverter.toEntity(agentDTO);

            LocalDateTime now = LocalDateTime.now();
            agent.setCreatedAt(now);
            agent.setUpdatedAt(now);

            // 插入数据库
            int result = agentMapper.insert(agent);
            if (result <= 0) {
                throw new BaseException("创建agent失败");
            }

            // 返回生成的agentId
            return CreateAgentResponse.builder()
                    .agentId(agent.getId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("创建agent时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void updateAgent(String agentId, UpdateAgentRequest request) {
        try {
            Agent existingAgent = agentMapper.selectById(agentId);
            if (existingAgent == null) {
                throw new BaseException("Agent不存在: " + agentId);
            }

            AgentDTO agentDTO = agentConverter.toDTO(existingAgent);
            agentConverter.updateDTOFromRequest(agentDTO, request);
            Agent updatedAgent = agentConverter.toEntity(agentDTO);

            // 保留Id和创建时间，更新修改时间
            updatedAgent.setId(existingAgent.getId());
            updatedAgent.setCreatedAt(existingAgent.getCreatedAt());
            updatedAgent.setUpdatedAt(LocalDateTime.now());

            int result = agentMapper.updateById(updatedAgent);
            if (result <= 0) {
                throw new BaseException("更新agent失败");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException("更新agent时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void deleteAgent(String agentId) {
        Agent existingAgent = agentMapper.selectById(agentId);
        if (existingAgent == null) {
            throw new BaseException("Agent不存在:" + agentId);
        }

        int result = agentMapper.deleteById(agentId);
        if (result <= 0) {
            throw new BaseException("删除agent失败");
        }
    }
}
