package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 智能体
* */
@Data
@Builder
public class Agent {
    private String id;

    private String name;

    private String description;

    private String systemPrompt;

    private String model;

    // JSON String
    private String allowedTools;

    // JSON String
//    private String allowedKbs;

    // JSON String
    private String chatOptions;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
