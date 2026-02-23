package org.jragent.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jragent.model.entity.Agent;

import java.util.List;

@Mapper
public interface AgentMapper {
    Agent selectById(@Param("id") String id);

    List<Agent> selectAll();

    int insert(Agent agent);

    int updateById(Agent agent);

    int deleteById(@Param("id") String id);
}
