package org.jragent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.jragent.model.common.ApiResponse;
import org.jragent.model.dto.document.CreateDocumentRequest;
import org.jragent.model.dto.document.UpdateDocumentRequest;
import org.jragent.model.vo.document.CreateDocumentResponse;
import org.jragent.model.vo.document.GetDocumentsResponse;
import org.jragent.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
@Tag(name = "文档", description = "文档管理与上传接口")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/documents/kb/{kbId}")
    @Operation(summary = "按知识库查询文档")
    public ApiResponse<GetDocumentsResponse> getDocumentsByKbId(@Parameter(description = "知识库ID", required = true) @PathVariable String kbId) {
        return ApiResponse.success(documentService.getDocumentsByKbId(kbId));
    }

    @GetMapping("/documents")
    @Operation(summary = "查询全部文档")
    public ApiResponse<GetDocumentsResponse> getDocuments() {
        return ApiResponse.success(documentService.getDocuments());
    }

    @PostMapping("/documents")
    @Operation(summary = "创建文档记录")
    public ApiResponse<CreateDocumentResponse> createDocument(@Valid @RequestBody CreateDocumentRequest request) {
        return ApiResponse.success(documentService.createDocument(request));
    }

    @PostMapping(value = "/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文档")
    public ApiResponse<CreateDocumentResponse> uploadDocument(
            @Parameter(description = "知识库ID", required = true) @NotBlank(message = "知识库ID不能为空") @RequestParam("kbId") String kbId,
            @Parameter(description = "上传文件", required = true) @NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(documentService.uploadDocument(kbId, file));
    }

    @PatchMapping("/documents/{documentId}")
    @Operation(summary = "更新文档")
    public ApiResponse<Void> updateDocument(
            @Parameter(description = "文档ID", required = true) @PathVariable String documentId,
            @Valid @RequestBody UpdateDocumentRequest request
    ) {
        documentService.updateDocument(documentId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/documents/{documentId}")
    @Operation(summary = "删除文档")
    public ApiResponse<Void> deleteDocument(@Parameter(description = "文档ID", required = true) @PathVariable String documentId) {
        documentService.deleteDocument(documentId);
        return ApiResponse.success();
    }
}
