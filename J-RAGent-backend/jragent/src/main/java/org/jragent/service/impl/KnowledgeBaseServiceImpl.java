package org.jragent.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.jragent.converter.KnowledgeBaseConverter;
import org.jragent.exception.BaseException;
import org.jragent.mapper.KnowledgeBaseMapper;
import org.jragent.model.dto.knowledgeBase.CreateOrUpdateKnowledgeBaseRequest;
import org.jragent.model.dto.knowledgeBase.KnowledgeBaseDTO;
import org.jragent.model.entity.KnowledgeBase;
import org.jragent.model.vo.knowledgeBase.CreateKnowledgeBaseResponse;
import org.jragent.model.vo.knowledgeBase.GetKnowledgeBasesResponse;
import org.jragent.model.vo.knowledgeBase.KnowledgeBaseVO;
import org.jragent.service.KnowledgeBaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    private final KnowledgeBaseMapper knowledgeBaseMapper;

    private final KnowledgeBaseConverter knowledgeBaseConverter;

    @Override
    public GetKnowledgeBasesResponse getKnowledgeBases() {
        List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.selectAll();
        List<KnowledgeBaseVO> knowledgeBaseVOS = knowledgeBases.stream().map(knowledgeBase -> {
            KnowledgeBaseVO knowledgeBaseVO;
            try {
                return knowledgeBaseConverter.toVO(knowledgeBase);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return GetKnowledgeBasesResponse.builder()
                .knowledgeBases(knowledgeBaseVOS)
                .build();
    }

    @Override
    public CreateKnowledgeBaseResponse createKnowledgeBase(CreateOrUpdateKnowledgeBaseRequest request) {
        try {
            KnowledgeBaseDTO knowledgeBaseDTO = knowledgeBaseConverter.toDTO(request);
            KnowledgeBase knowledgeBase = knowledgeBaseConverter.toEntity(knowledgeBaseDTO);

            LocalDateTime now = LocalDateTime.now();
            knowledgeBase.setCreatedAt(now);
            knowledgeBase.setUpdatedAt(now);

            int result = knowledgeBaseMapper.insert(knowledgeBase);
            if (result <= 0) {
                throw new BaseException("创建知识库失败");
            }

            return CreateKnowledgeBaseResponse.builder()
                    .knowledgeBaseId(knowledgeBase.getId())
                    .build();
        } catch (JsonProcessingException e) {
            throw new BaseException("创建知识库时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void updateKnowledgeBase(String knowledgeBaseId, CreateOrUpdateKnowledgeBaseRequest request) {
        try {
            KnowledgeBase existingKnowledgeBase = knowledgeBaseMapper.selectById(knowledgeBaseId);
            if (existingKnowledgeBase == null) {
                throw new BaseException("知识库不存在: " + knowledgeBaseId);
            }

            KnowledgeBaseDTO knowledgeBaseDTO = knowledgeBaseConverter.toDTO(existingKnowledgeBase);
            knowledgeBaseConverter.updateDTOFromRequest(knowledgeBaseDTO, request);
            KnowledgeBase updatedKnowledgeBase = knowledgeBaseConverter.toEntity(knowledgeBaseDTO);

            updatedKnowledgeBase.setId(existingKnowledgeBase.getId());
            updatedKnowledgeBase.setCreatedAt(existingKnowledgeBase.getCreatedAt());
            updatedKnowledgeBase.setUpdatedAt(LocalDateTime.now());

            int result = knowledgeBaseMapper.updateById(updatedKnowledgeBase);
            if (result <= 0) {
                throw new BaseException("更新知识库失败");
            }
        } catch (JsonProcessingException e) {
            throw new BaseException("更新知识库时发生序列化错误: " + e.getMessage());
        }
    }

    @Override
    public void deleteKnowledgeBase(String knowledgeBaseId) {
        KnowledgeBase knowledgeBase = knowledgeBaseMapper.selectById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new BaseException("知识库不存在: " + knowledgeBaseId);
        }

        int result = knowledgeBaseMapper.deleteById(knowledgeBaseId);
        if (result <= 0) {
            throw new BaseException("删除知识库失败");
        }
    }
}
