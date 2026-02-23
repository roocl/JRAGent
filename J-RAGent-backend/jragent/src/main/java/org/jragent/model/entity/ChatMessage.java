package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 单次聊天消息
* */
@Data
@Builder
public class ChatMessage {
    private String id;

    private String sessionId;

    private String role;

    private String content;

    // JSON String
    private String metadata;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
