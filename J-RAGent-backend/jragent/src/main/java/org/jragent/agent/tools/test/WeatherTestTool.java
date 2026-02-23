package org.jragent.agent.tools.test;

import org.jragent.agent.tools.Tool;
import org.jragent.agent.tools.ToolType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherTestTool implements Tool {

    @Override
    public String getName() {
        return "WeatherTestTool";
    }

    @Override
    public String getDescription() {
        return "获取天气";
    }

    @Override
    public ToolType getType() {
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "weather", description = "获取天气")
    public String getWeather(String city, String date) {
        Map<String, String> map = new HashMap<>();
        map.put("深圳", "天气查询结果：晴转多云，温度 25°C，湿度 60%");
        map.put("厦门", "天气查询结果：晴，温度 30°C，湿度 75%");
        map.put("上海", "天气查询结果：多云，温度 18°C，湿度 80%");
        map.put("北京", "天气查询结果：小雨，温度 20°C，湿度 88%");
        return city + date + map.get(city);
    }
}
