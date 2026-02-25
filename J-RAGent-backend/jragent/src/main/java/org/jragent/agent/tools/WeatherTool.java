package org.jragent.agent.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jragent.client.WeatherClient;
import org.jragent.model.vo.weather.WeatherDailyResponse;
import org.jragent.model.vo.weather.WeatherNowResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherTool implements Tool {

    private final WeatherClient weatherClient;

    private final ObjectMapper objectMapper;

    @Override
    public String getName() {
        return "WeatherTool";
    }

    @Override
    public String getDescription() {
        return "获取天气";
    }

    @Override
    public ToolType getType() {
        return ToolType.OPTIONAL;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "weather", description = "根据城市获取天气")
    public String getWeather(String city) {
        try {
            String locationId = weatherClient.getLocationId(city);
            WeatherNowResponse weatherNow = weatherClient.getNowWeather(locationId);
            WeatherDailyResponse todayForecast = weatherClient.getTodayForecast(locationId);

            ObjectNode result = objectMapper.createObjectNode();
            result.put("success", true);
            result.put("city", city);
            result.put("locationId", locationId);
            result.set("now", objectMapper.valueToTree(weatherNow));
            result.set("todayForecast", objectMapper.valueToTree(todayForecast));
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            log.error("查询天气失败", e);
            ObjectNode error = objectMapper.createObjectNode();
            error.put("success", false);
            error.put("city", city);
            error.put("error", "调用和风天气API失败，暂时无法提供天气信息");
            error.put("detail", e.getMessage());
            return error.toString();
        }
    }
}
