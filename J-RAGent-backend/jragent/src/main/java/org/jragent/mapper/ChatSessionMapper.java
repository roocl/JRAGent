package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.ChatSession;

import java.util.List;

@Mapper
public interface ChatSessionMapper {
    ChatSession selectById(@Param("id") String id);

    List<ChatSession> selectByAgentId(String agentId);

    List<ChatSession> selectAll();

    int insert(ChatSession chatSession);

    int updateById(ChatSession chatSession);

    int deleteById(@Param("id") String id);
}
