package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.ChatMessage;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    ChatMessage selectById(@Param("id") String id);

    List<ChatMessage> selectBySessionId(@Param("sessionId") String sessionId);

    List<ChatMessage> selectBySessionIdRecently(@Param("sessionId") String sessionId, @Param("limit") int limit);

    int insert(ChatMessage chatMessage);

    int updateById(ChatMessage chatMessage);

    int deleteById(@Param("id") String id);
}
