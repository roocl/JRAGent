package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.jragent.converter.AgentConverter;
import org.jragent.exception.BaseException;
import org.jragent.mapper.AgentMapper;
import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.entity.Agent;
import org.jragent.model.dto.AgentDTO;
import org.jragent.model.vo.CreateAgentResponse;
import org.jragent.service.AgentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentMapper agentMapper;

    private final AgentConverter agentConverter;

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
                throw new BaseException("创建 agent 失败");
            }

            // 返回生成的 agentId
            return CreateAgentResponse.builder()
                    .agentId(agent.getId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("创建 agent 时发生序列化错误: " + e.getMessage());
        }
    }
}
