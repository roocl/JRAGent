package org.jragent.model.vo.knowledgeBase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "KnowledgeBaseVO", description = "知识库展示对象")
public class KnowledgeBaseVO {
    @Schema(description = "知识库ID", example = "kb_001")
    private String id;

    @Schema(description = "知识库名称", example = "旅游景点知识库")
    private String name;

    @Schema(description = "知识库描述", example = "存储各城市景点介绍文档")
    private String description;
}
