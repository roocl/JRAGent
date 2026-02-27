package org.jragent.model.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateDocumentRequest", description = "更新文档请求")
public class UpdateDocumentRequest {
    @Size(max = 255, message = "文件名长度不能超过255")
    @Schema(description = "文件名", example = "shenzhen_spots_v2.md")
    private String filename;

    @Size(max = 20, message = "文件类型长度不能超过20")
    @Schema(description = "文件类型", example = ".md")
    private String filetype;

    @PositiveOrZero(message = "文件大小不能为负数")
    @Schema(description = "文件大小（字节）", example = "20480")
    private Long size;
}

