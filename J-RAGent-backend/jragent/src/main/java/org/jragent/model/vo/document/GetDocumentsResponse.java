package org.jragent.model.vo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(name = "GetDocumentsResponse", description = "获取文档列表响应")
public class GetDocumentsResponse {
    @Schema(description = "文档列表")
    private List<DocumentVO> documents;
}
