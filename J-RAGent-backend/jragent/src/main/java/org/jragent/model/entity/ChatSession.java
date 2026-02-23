package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 聊天会话
* */
@Data
@Builder
public class ChatSession {
    private String id;

    private String agentId;

    private String title;

    // JSON string
    private String metadata;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
