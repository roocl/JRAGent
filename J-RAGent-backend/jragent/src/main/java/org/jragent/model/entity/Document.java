package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/*
* 文档
* */
@Data
@Builder
public class Document {
    private String id;

    private String kbId;

    private String filename;

    private String filetype;

    private Long size;

    // JSON String
    private String metadata;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
