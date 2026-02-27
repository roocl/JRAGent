package org.jragent.service;

import org.jragent.model.dto.document.CreateDocumentRequest;
import org.jragent.model.dto.document.UpdateDocumentRequest;
import org.jragent.model.vo.document.CreateDocumentResponse;
import org.jragent.model.vo.document.GetDocumentsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    GetDocumentsResponse getDocumentsByKbId(String kbId);

    GetDocumentsResponse getDocuments();

    CreateDocumentResponse createDocument(CreateDocumentRequest request);

    CreateDocumentResponse uploadDocument(String kbId, MultipartFile file);

    void updateDocument(String documentId, UpdateDocumentRequest request);

    void deleteDocument(String documentId);
}
