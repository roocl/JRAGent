package org.jragent.model.vo.knowledgeBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetKnowledgeBasesResponse", description = "获取知识库列表响应")
public class GetKnowledgeBasesResponse {
    @Schema(description = "知识库列表")
    private List<KnowledgeBaseVO> knowledgeBases;
}
