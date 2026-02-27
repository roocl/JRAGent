package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.ChunkBgeM3;

import java.util.List;

@Mapper
public interface ChunkBgeM3Mapper {
    ChunkBgeM3 selectById(@Param("id") String id);

    int insert(ChunkBgeM3 chunkBgeM3);

    int updateById(ChunkBgeM3 chunkBgeM3);

    int deleteById(@Param("id") String id);

    List<ChunkBgeM3> similaritySearch(
            @Param("kbId") String kbId,
            @Param("vectorLiteral") String vectorLiteral,
            @Param("limit") int limit
    );
}
