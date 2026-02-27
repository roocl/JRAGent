package org.jragent.service;

import org.jragent.model.dto.knowledgeBase.CreateOrUpdateKnowledgeBaseRequest;
import org.jragent.model.vo.knowledgeBase.CreateKnowledgeBaseResponse;
import org.jragent.model.vo.knowledgeBase.GetKnowledgeBasesResponse;

public interface KnowledgeBaseService {
    GetKnowledgeBasesResponse getKnowledgeBases();

    CreateKnowledgeBaseResponse createKnowledgeBase(CreateOrUpdateKnowledgeBaseRequest request);

    void updateKnowledgeBase(String knowledgeBaseId, CreateOrUpdateKnowledgeBaseRequest request);

    void deleteKnowledgeBase(String knowledgeBaseId);
}
