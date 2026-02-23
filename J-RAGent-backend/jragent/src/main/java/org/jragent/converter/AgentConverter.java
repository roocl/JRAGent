package org.jragent.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.jragent.model.dto.AgentDTO;
import org.jragent.model.dto.CreateAgentRequest;
import org.jragent.model.entity.Agent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@AllArgsConstructor
public class AgentConverter {

    private final ObjectMapper objectMapper;

    public Agent toEntity(AgentDTO agentDTO) throws JsonProcessingException {
        Assert.notNull(agentDTO, "AgentDTO cannot be null");
        Assert.notNull(agentDTO.getAllowedTools(), "Allowed tools cannot be null");
//        Assert.notNull(agentDTO.getAllowedKbs(), "Allowed kbs cannot be null");
        Assert.notNull(agentDTO.getChatOptions(), "Chat options cannot be null");
        Assert.notNull(agentDTO.getModel(), "Model cannot be null");

        return Agent.builder()
                .id(agentDTO.getId())
                .name(agentDTO.getName())
                .description(agentDTO.getDescription())
                .systemPrompt(agentDTO.getSystemPrompt())
                .model(agentDTO.getModel().getModelName())
                .allowedTools(objectMapper.writeValueAsString(agentDTO.getAllowedTools()))
//                .allowedKbs(objectMapper.writeValueAsString(agentDTO.getAllowedKbs()))
                .chatOptions(objectMapper.writeValueAsString(agentDTO.getChatOptions()))
                .createdAt(agentDTO.getCreatedAt())
                .updatedAt(agentDTO.getUpdatedAt())
                .build();
    }

    public AgentDTO toDTO(Agent agent) throws JsonProcessingException {
        Assert.notNull(agent, "Agent cannot be null");
        Assert.notNull(agent.getAllowedTools(), "Allowed tools cannot be null");
//        Assert.notNull(agent.getAllowedKbs(), "Allowed kbs cannot be null");
        Assert.notNull(agent.getChatOptions(), "Chat options cannot be null");
        Assert.notNull(agent.getModel(), "Model cannot be null");

        return AgentDTO.builder()
                .id(agent.getId())
                .name(agent.getName())
                .description(agent.getDescription())
                .systemPrompt(agent.getSystemPrompt())
                .model(AgentDTO.ModelType.fromModelName(agent.getModel()))
                .allowedTools(objectMapper.readValue(agent.getAllowedTools(), new TypeReference<>(){}))
//                .allowedKbs(objectMapper.readValue(agent.getAllowedKbs(), new TypeReference<>(){}))
                .chatOptions(objectMapper.readValue(agent.getChatOptions(), AgentDTO.ChatOptions.class))
                .createdAt(agent.getCreatedAt())
                .updatedAt(agent.getUpdatedAt())
                .build();
    }

    public AgentDTO toDTO(CreateAgentRequest request) {
        Assert.notNull(request, "CreateAgentRequest cannot be null");
        Assert.notNull(request.getAllowedTools(), "Allowed tools cannot be null");
//        Assert.notNull(request.getAllowedKbs(), "Allowed kbs cannot be null");
        Assert.notNull(request.getChatOptions(), "Chat options cannot be null");
        Assert.notNull(request.getModel(), "Model cannot be null");

        return AgentDTO.builder()
                .name(request.getName())
                .description(request.getDescription())
                .systemPrompt(request.getSystemPrompt())
                .model(AgentDTO.ModelType.fromModelName(request.getModel()))
                .allowedTools(request.getAllowedTools())
//                .allowedKbs(request.getAllowedKbs())
                .chatOptions(request.getChatOptions())
                .build();
    }

}
