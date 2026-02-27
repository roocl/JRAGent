package org.jragent.model.vo.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "DocumentVO", description = "文档展示对象")
public class DocumentVO {
    @Schema(description = "文档ID", example = "doc_001")
    private String id;

    @Schema(description = "知识库ID", example = "kb_001")
    private String kbId;

    @Schema(description = "文件名", example = "shenzhen_spots.md")
    private String filename;

    @Schema(description = "文件类型", example = ".md")
    private String filetype;

    @Schema(description = "文件大小（字节）", example = "10240")
    private Long size;
}
