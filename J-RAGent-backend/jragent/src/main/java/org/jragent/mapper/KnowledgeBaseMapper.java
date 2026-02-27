package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.KnowledgeBase;

import java.util.List;

@Mapper
public interface KnowledgeBaseMapper {
    KnowledgeBase selectById(@Param("id") String id);

    List<KnowledgeBase> selectByIdBatch(List<String> ids);

    List<KnowledgeBase> selectAll();

    int insert(KnowledgeBase knowledgeBase);

    int updateById(KnowledgeBase knowledgeBase);

    int deleteById(@Param("id") String id);
}
