package org.jragent.model.vo.knowledgeBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateKnowledgeBaseResponse", description = "创建知识库响应")
public class CreateKnowledgeBaseResponse {
    @Schema(description = "生成的知识库ID", example = "kb_001")
    private String knowledgeBaseId;
}
