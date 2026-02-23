package org.jragent.agent.tools.test;

import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CityTestTool implements Tool {
    @Override
    public String getName() {
        return "CityTestTool";
    }

    @Override
    public String getDescription() {
        return "获取当前的城市列表";
    }

    @Override
    public ToolType getType() {
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "getCity", description = "获取当前的城市")
    public Set<String> getCity() {
        Set<String> city = new HashSet<>();
        city.add("深圳");
        city.add("厦门");
        city.add("上海");
        city.add("北京");
        return city;
    }

}
