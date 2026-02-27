package org.jragent.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChunkBgeM3 {
    private String id;

    private String kbId;

    private String docId;

    private String content;

    private String metadata;

    private float[] embedding;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
