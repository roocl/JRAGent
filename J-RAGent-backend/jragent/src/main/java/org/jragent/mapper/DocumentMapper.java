package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.Document;

import java.util.List;

@Mapper
public interface DocumentMapper {
    Document selectById(@Param("id") String id);

    List<Document> selectByKbId(String kbId);

    List<Document> selectAll();

    int insert(Document document);

    int updateById(Document document);

    int deleteById(@Param("id") String id);
}
