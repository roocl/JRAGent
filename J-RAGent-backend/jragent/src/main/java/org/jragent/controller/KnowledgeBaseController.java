package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.knowledgeBase.CreateOrUpdateKnowledgeBaseRequest;
import org.jragent.model.vo.knowledgeBase.CreateKnowledgeBaseResponse;
import org.jragent.model.vo.knowledgeBase.GetKnowledgeBasesResponse;
import org.jragent.service.KnowledgeBaseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "知识库", description = "知识库管理接口")
public class KnowledgeBaseController {
    private final KnowledgeBaseService knowledgeBaseService;

    @GetMapping("/knowledge-bases")
    @Operation(summary = "查询知识库列表")
    public ApiResponse<GetKnowledgeBasesResponse> getKnowledgeBases() {
        return ApiResponse.success(knowledgeBaseService.getKnowledgeBases());
    }

    @PostMapping("/knowledge-bases")
    @Operation(summary = "创建知识库")
    public ApiResponse<CreateKnowledgeBaseResponse> createKnowledgeBase(@Valid @RequestBody CreateOrUpdateKnowledgeBaseRequest request) {
        return ApiResponse.success(knowledgeBaseService.createKnowledgeBase(request));
    }

    @PatchMapping("/knowledge-bases/{knowledgeBaseId}")
    @Operation(summary = "更新知识库")
    public ApiResponse<Void> updateKnowledgeBase(
            @Parameter(description = "知识库ID", required = true) @PathVariable String knowledgeBaseId,
            @Valid @RequestBody CreateOrUpdateKnowledgeBaseRequest request
    ) {
        knowledgeBaseService.updateKnowledgeBase(knowledgeBaseId, request);
        return ApiResponse.success();
    }
    
    @DeleteMapping("/knowledge-bases/{knowledgeBaseId}")
    @Operation(summary = "删除知识库")
    public ApiResponse<Void> deleteKnowledgeBase(@Parameter(description = "知识库ID", required = true) @PathVariable String knowledgeBaseId) {
        knowledgeBaseService.deleteKnowledgeBase(knowledgeBaseId);
        return ApiResponse.success();
    }
}
