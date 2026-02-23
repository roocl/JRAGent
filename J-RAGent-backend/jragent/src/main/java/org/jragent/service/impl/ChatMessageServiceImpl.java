package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.jragent.converter.ChatMessageConverter;
import org.jragent.event.ChatEvent;
import org.jragent.exception.BaseException;
import org.jragent.mapper.ChatMessageMapper;
import org.jragent.model.dto.ChatMessageDTO;
import org.jragent.model.dto.CreateChatMessageRequest;
import org.jragent.model.dto.UpdateChatMessageRequest;
import org.jragent.model.entity.ChatMessage;
import org.jragent.model.vo.CreateChatMessageResponse;
import org.jragent.service.ChatMessageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageMapper chatMessageMapper;

    private final ChatMessageConverter chatMessageConverter;

    private final ApplicationEventPublisher publisher;

    @Override
    public CreateChatMessageResponse createChatMessage(CreateChatMessageRequest request) {
        ChatMessage chatMessage = doCreateChatMessage(request);

        // 发布聊天通知事件
        publisher.publishEvent(new ChatEvent(request.getAgentId(),
                        chatMessage.getSessionId(),
                        chatMessage.getContent())
        );

        // 返回生成的chatMessageId
        return CreateChatMessageResponse.builder()
                .chatMessageId(chatMessage.getId())
                .build();
    }

    @Override
    public CreateChatMessageResponse createChatMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = doCreateChatMessage(chatMessageDTO);
        return CreateChatMessageResponse.builder()
                .chatMessageId(chatMessage.getId())
                .build();
    }

    private ChatMessage doCreateChatMessage(CreateChatMessageRequest request) {
        ChatMessageDTO chatMessageDTO = chatMessageConverter.toDTO(request);
        return doCreateChatMessage(chatMessageDTO);
    }

    private ChatMessage doCreateChatMessage(ChatMessageDTO chatMessageDTO) {
        try {
            ChatMessage chatMessage = chatMessageConverter.toEntity(chatMessageDTO);

            LocalDateTime now = LocalDateTime.now();
            chatMessage.setCreatedAt(now);
            chatMessage.setUpdatedAt(now);

            int result = chatMessageMapper.insert(chatMessage);
            if (result <= 0) {
                throw new BaseException("创建聊天消息失败");
            }

            return chatMessage;
        } catch (JsonProcessingException e) {
            throw new BaseException("创建聊天消息时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void updateChatMessage(String chatMessageId, UpdateChatMessageRequest request) {
        try {
            // 查询现有的聊天消息
            ChatMessage existingChatMessage = chatMessageMapper.selectById(chatMessageId);
            if (existingChatMessage == null) {
                throw new BaseException("聊天消息不存在: " + chatMessageId);
            }

            ChatMessageDTO chatMessageDTO = chatMessageConverter.toDTO(existingChatMessage);

            chatMessageConverter.updateDTOFromRequest(chatMessageDTO, request);

            ChatMessage updatedChatMessage = chatMessageConverter.toEntity(chatMessageDTO);

            // 保留原有的ID、sessionId、role 和创建时间
            updatedChatMessage.setId(existingChatMessage.getId());
            updatedChatMessage.setSessionId(existingChatMessage.getSessionId());
            updatedChatMessage.setRole(existingChatMessage.getRole());
            updatedChatMessage.setCreatedAt(existingChatMessage.getCreatedAt());
            updatedChatMessage.setUpdatedAt(LocalDateTime.now());

            // 更新数据库
            int result = chatMessageMapper.updateById(updatedChatMessage);
            if (result <= 0) {
                throw new BaseException("更新聊天消息失败");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException("更新聊天消息时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public List<ChatMessageDTO> getChatMessagesBySessionIdRecently(String sessionId, int limit) {
        List<ChatMessage> chatMessages = chatMessageMapper.selectBySessionIdRecently(sessionId, limit);
        List<ChatMessageDTO> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            try {
                ChatMessageDTO chatMessageDTO = chatMessageConverter.toDTO(chatMessage);
                result.add(chatMessageDTO);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


}
