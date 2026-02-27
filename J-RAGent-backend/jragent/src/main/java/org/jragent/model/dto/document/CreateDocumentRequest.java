package org.jragent.model.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CreateDocumentRequest", description = "创建文档请求")
public class CreateDocumentRequest {
    @NotBlank(message = "知识库ID不能为空")
    @Schema(description = "知识库ID", example = "kb_001")
    private String kbId;

    @NotBlank(message = "文件名不能为空")
    @Size(max = 255, message = "文件名长度不能超过255")
    @Schema(description = "文件名", example = "shenzhen_spots.md")
    private String filename;

    @NotBlank(message = "文件类型不能为空")
    @Size(max = 20, message = "文件类型长度不能超过20")
    @Schema(description = "文件类型", example = ".md")
    private String filetype;

    @NotNull(message = "文件大小不能为空")
    @PositiveOrZero(message = "文件大小不能为负数")
    @Schema(description = "文件大小（字节）", example = "10240")
    private Long size;
}

