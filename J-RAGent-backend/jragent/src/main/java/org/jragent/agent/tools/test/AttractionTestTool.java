package org.jragent.agent.tools.test;

import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AttractionTestTool implements Tool {
    @Override
    public String getName() {
        return "AttractionTestTool";
    }

    @Override
    public String getDescription() {
        return "获取当前城市的景点";
    }

    @Override
    public ToolType getType() {
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "getAttraction", description = "获取当前城市的景点")
    public Set<String> getAttraction(String city) {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> set1 = new HashSet<>();
        set1.add("国家博物馆，室内景点。");
        set1.add("天坛，户外景点。");
        set1.add("故宫，户外景点。");
        map.put("北京", set1);
        Set<String> set2 = new HashSet<>();
        set2.add("深圳公园，户外景点。");
        set2.add("深圳博物馆，室内景点。");
        map.put("深圳", set2);
        map.put("上海", new HashSet<>());
        map.put("厦门", new HashSet<>());
        return map.get(city);
    }
}