package org.jragent.model.dto.knowledgeBase;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CreateOrUpdateKnowledgeBaseRequest", description = "创建或更新知识库请求")
public class CreateOrUpdateKnowledgeBaseRequest {
    @NotBlank(message = "知识库名称不能为空")
    @Size(max = 100, message = "知识库名称长度不能超过100")
    @Schema(description = "知识库名称", example = "旅游景点知识库")
    private String name;

    @NotBlank(message = "知识库描述不能为空")
    @Size(max = 1000, message = "知识库描述长度不能超过1000")
    @Schema(description = "知识库描述", example = "用于存储各城市景点介绍与出行建议")
    private String description;
}
