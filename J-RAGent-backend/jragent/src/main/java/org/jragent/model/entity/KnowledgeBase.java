package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 知识库
* */
@Data
@Builder
public class KnowledgeBase {
    private String id;

    private String name;

    private String description;

    private String metadata;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
