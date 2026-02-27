package org.jragent.model.vo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "CreateDocumentResponse", description = "创建文档响应")
public class CreateDocumentResponse {
    @Schema(description = "生成的文档ID", example = "doc_001")
    private String documentId;
}
