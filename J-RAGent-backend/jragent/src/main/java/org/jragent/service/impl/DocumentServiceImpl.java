package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jragent.converter.DocumentConverter;
import org.jragent.exception.BaseException;
import org.jragent.mapper.ChunkBgeM3Mapper;
import org.jragent.mapper.DocumentMapper;
import org.jragent.model.dto.document.CreateDocumentRequest;
import org.jragent.model.dto.document.DocumentDTO;
import org.jragent.model.dto.document.UpdateDocumentRequest;
import org.jragent.model.entity.ChunkBgeM3;
import org.jragent.model.entity.Document;
import org.jragent.model.vo.document.CreateDocumentResponse;
import org.jragent.model.vo.document.DocumentVO;
import org.jragent.model.vo.document.GetDocumentsResponse;
import org.jragent.service.DocumentService;
import org.jragent.service.DocumentStorageService;
import org.jragent.service.MarkdownParserService;
import org.jragent.service.RagService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMapper documentMapper;

    private final DocumentConverter documentConverter;

    private final DocumentStorageService documentStorageService;

    private final MarkdownParserService markdownParserService;

    private final RagService ragService;

    private final ChunkBgeM3Mapper chunkBgeM3Mapper;


    @Override
    public GetDocumentsResponse getDocumentsByKbId(String kbId) {
        List<Document> documents = documentMapper.selectByKbId(kbId);

        List<DocumentVO> documentVOS = documents.stream().map(document -> {
            try {
                return documentConverter.toVO(document);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return GetDocumentsResponse.builder()
                .documents(documentVOS)
                .build();
    }

    @Override
    public GetDocumentsResponse getDocuments() {
        List<Document> documents = documentMapper.selectAll();

        List<DocumentVO> documentVOS = documents.stream().map(document -> {
            try {
                return documentConverter.toVO(document);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return GetDocumentsResponse.builder()
                .documents(documentVOS)
                .build();
    }


    @Override
    public CreateDocumentResponse createDocument(CreateDocumentRequest request) {
        try {
            DocumentDTO documentDTO = documentConverter.toDTO(request);
            Document document = documentConverter.toEntity(documentDTO);

            LocalDateTime now = LocalDateTime.now();
            document.setCreatedAt(now);
            document.setUpdatedAt(now);

            int result = documentMapper.insert(document);
            if (result <= 0) {
                throw new BaseException("创建文档失败");
            }

            return CreateDocumentResponse.builder()
                    .documentId(document.getId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("创建文档时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public CreateDocumentResponse uploadDocument(String kbId, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new BaseException("上传的文件为空");
            }

            // 文档信息
            String fileName = file.getOriginalFilename();
            String fileType = (fileName == null || !fileName.contains(".")) ? "unknown" : fileName.substring(fileName.lastIndexOf(".") + 1);
            long fileSize = file.getSize();

            // 先创建文档记录以获取id
            DocumentDTO documentDTO = DocumentDTO.builder()
                    .kbId(kbId)
                    .filename(fileName)
                    .filetype(fileType)
                    .size(fileSize)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Document document = documentConverter.toEntity(documentDTO);

            int result = documentMapper.insert(document);
            if (result <= 0) {
                throw new BaseException("创建文档记录失败");
            }

            String documentId = document.getId();

            // 保存文档
            String filePath = documentStorageService.saveFile(kbId, documentId, file);

            DocumentDTO.MetaData metadata = new DocumentDTO.MetaData();
            metadata.setFilePath(filePath);
            documentDTO.setMetadata(metadata);
            documentDTO.setId(documentId);
            documentDTO.setUpdatedAt(LocalDateTime.now());

            Document updatedDocument = documentConverter.toEntity(documentDTO);

            documentMapper.updateById(updatedDocument);

            log.info("文档上传成功: kbId={}, documentId={}, filename={}", kbId, documentId, fileName);

            // 文档解析
            if ("md".equalsIgnoreCase(fileType) || "markdown".equalsIgnoreCase(fileType)) {
                processMarkdownDocument(kbId, documentId, filePath);
            } else {
                // TODO 其他文件
                log.warn("待新增处理的文档类型: {}", fileType);
            }

            return CreateDocumentResponse.builder()
                    .documentId(documentId)
                    .build();
        } catch (IOException e) {
            log.error("文档保存失败", e);
            throw new BaseException("文档保存失败: " + e.getMessage());
        }
    }


    private void processMarkdownDocument(String kbId, String documentId, String filePath) {
        try {
            log.info("开始处理 Markdown 文档: kbId={}, documentId={}, filePath={}", kbId, documentId, filePath);

            // 读取文件
            Path path = documentStorageService.getFilePath(filePath);
            try (InputStream inputStream = Files.newInputStream(path)) {
                List<MarkdownParserService.MarkdownSection> sections = markdownParserService.parseMarkdown(inputStream);

                log.info("文档内容:\n");
                log.info(sections.toString());

                if (sections.isEmpty()) {
                    log.warn("Markdown文档解析后没有找到任何章节: documentId={}", documentId);
                    return;
                }

                LocalDateTime now = LocalDateTime.now();
                int chunkCount = 0;

                // 为每个章节生成chunk
                for (MarkdownParserService.MarkdownSection section : sections) {
                    String title = section.getTitle();
                    String content = section.getContent();

                    if (title == null || title.trim().isEmpty()) {
                        continue;
                    }

                    // 标题向量化
                    float[] embedding = ragService.embed(title);

                    ChunkBgeM3 chunk = ChunkBgeM3.builder()
                            .kbId(kbId)
                            .docId(documentId)
                            .content(content)
                            .embedding(embedding)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                    int result = chunkBgeM3Mapper.insert(chunk);

                    if (result <= 0) {
                        log.warn("创建chunk失败: title={}, contentId={}", title, chunk.getId());
                    } else {
                        log.debug("创建chunk成: title={}, contentId={}", title, chunk.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理 Markdown 文档失败: documentId={}", documentId, e);
        }
    }

    @Override
    public void updateDocument(String documentId, UpdateDocumentRequest request) {
        try {
            Document existingDocument = documentMapper.selectById(documentId);
            if (existingDocument == null) {
                throw new BaseException("文档不存在: " + documentId);
            }

            DocumentDTO documentDTO = documentConverter.toDTO(existingDocument);
            documentConverter.updateDTOFromRequest(documentDTO, request);
            Document updatedDocument = documentConverter.toEntity(documentDTO);

            updatedDocument.setId(existingDocument.getId());
            updatedDocument.setKbId(existingDocument.getKbId());
            updatedDocument.setCreatedAt(existingDocument.getCreatedAt());
            updatedDocument.setUpdatedAt(LocalDateTime.now());

            int result = documentMapper.updateById(updatedDocument);
            if (result <= 0) {
                throw new BaseException("更新文档失败");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException("更新文档时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void deleteDocument(String documentId) {
        Document document = documentMapper.selectById(documentId);
        if (document == null) {
            throw new BaseException("文档不存在: " + documentId);
        }

        try {
            DocumentDTO documentDTO = documentConverter.toDTO(document);
            if (documentDTO.getMetadata() != null && documentDTO.getMetadata().getFilePath() != null) {
                String filePath = documentDTO.getMetadata().getFilePath();
                documentStorageService.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.warn("删除文件失败，继续删除文档记录: documentId={}, error={}", documentId, e.getMessage());
        }

        int result = documentMapper.deleteById(documentId);
        if (result <= 0) {
            throw new BaseException("删除文档失败");
        }
    }
}
