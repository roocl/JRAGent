package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.jragent.converter.ChatSessionConverter;
import org.jragent.exception.BaseException;
import org.jragent.mapper.AgentMapper;
import org.jragent.mapper.ChatSessionMapper;
import org.jragent.model.dto.chatSession.ChatSessionDTO;
import org.jragent.model.dto.chatSession.CreateChatSessionRequest;
import org.jragent.model.dto.chatSession.UpdateChatSessionRequest;
import org.jragent.model.entity.Agent;
import org.jragent.model.entity.ChatSession;
import org.jragent.model.vo.chatSession.ChatSessionVO;
import org.jragent.model.vo.chatSession.CreateChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionResponse;
import org.jragent.model.vo.chatSession.GetChatSessionsResponse;
import org.jragent.service.ChatSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionMapper chatSessionMapper;

    private final AgentMapper agentMapper;

    private final ChatSessionConverter chatSessionConverter;

    @Override
    public GetChatSessionResponse getChatSession(String chatSessionId) {
        ChatSession chatSession = chatSessionMapper.selectById(chatSessionId);
        if (chatSession == null) {
            throw new BaseException("聊天会话不存在: " + chatSessionId);
        }

        try {
            ChatSessionVO chatSessionVO = chatSessionConverter.toVO(chatSession);
            return GetChatSessionResponse.builder()
                    .chatSession(chatSessionVO)
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException();
        }
    }

    @Override
    public GetChatSessionsResponse getChatSessionsByAgentId(String agentId) {
        List<ChatSession> chatSessions = chatSessionMapper.selectByAgentId(agentId);
        return toResponseFromEntities(chatSessions);
    }

    @Override
    public GetChatSessionsResponse getChatSessions() {
        List<ChatSession> chatSessions = chatSessionMapper.selectAll();
        return toResponseFromEntities(chatSessions);
    }

    //entities -> vos -> response
    private GetChatSessionsResponse toResponseFromEntities(List<ChatSession> chatSessions) {
        List<ChatSessionVO> chatSessionVOS = chatSessions.stream().map(chatSession -> {
            ChatSessionVO chatSessionVO;
            try {
                chatSessionVO = chatSessionConverter.toVO(chatSession);
                return chatSessionVO;
            } catch (JsonProcessingException e) {
                throw new BaseException();
            }
        }).toList();

        return GetChatSessionsResponse.builder()
                .chatSessions(chatSessionVOS)
                .build();
    }

    @Override
    public CreateChatSessionResponse createChatSession(CreateChatSessionRequest request) {
        try {
            Agent agent = agentMapper.selectById(request.getAgentId());
            if (agent == null) {
                throw new BaseException("agent不存在: " + request.getAgentId());
            }

            // req转dto
            ChatSessionDTO chatSessionDTO = chatSessionConverter.toDTO(request);

            // dto转entity
            ChatSession chatSession = chatSessionConverter.toEntity(chatSessionDTO);

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            chatSession.setCreatedAt(now);
            chatSession.setUpdatedAt(now);

            // 插入数据库
            int result = chatSessionMapper.insert(chatSession);
            if (result <= 0) {
                throw new BaseException("创建聊天会话失败");
            }

            // 返回生成的 chatSessionId
            return CreateChatSessionResponse.builder()
                    .chatSessionId(chatSession.getId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("创建聊天会话时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void updateChatSession(String chatSessionId, UpdateChatSessionRequest request) {
        try {
            // 查询现有的聊天会话
            ChatSession existingChatSession = chatSessionMapper.selectById(chatSessionId);
            if (existingChatSession == null) {
                throw new BaseException("聊天会话不存在: " + chatSessionId);
            }

            // entity转dto
            ChatSessionDTO chatSessionDTO = chatSessionConverter.toDTO(existingChatSession);

            // req更新dto
            chatSessionConverter.updateDTOFromRequest(chatSessionDTO, request);

            // dto转entity
            ChatSession updatedChatSession = chatSessionConverter.toEntity(chatSessionDTO);

            // 保留原有的ID、agentId和创建时间
            updatedChatSession.setId(existingChatSession.getId());
            updatedChatSession.setAgentId(existingChatSession.getAgentId());
            updatedChatSession.setCreatedAt(existingChatSession.getCreatedAt());
            updatedChatSession.setUpdatedAt(LocalDateTime.now());

            // 更新数据库
            int result = chatSessionMapper.updateById(updatedChatSession);
            if (result <= 0) {
                throw new BaseException("更新聊天会话失败");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException("更新聊天会话时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void deleteChatSession(String chatSessionId) {
        ChatSession existingChatSession = chatSessionMapper.selectById(chatSessionId);
        if (existingChatSession == null) {
            throw new BaseException("聊天会话不存在: " + chatSessionId);
        }

        int result = chatSessionMapper.deleteById(existingChatSession.getId());
        if (result <= 0) {
            throw new BaseException("删除聊天会话失败");
        }
    }
}
