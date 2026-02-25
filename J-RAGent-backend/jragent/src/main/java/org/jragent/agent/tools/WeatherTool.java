package org.jragent.agent.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jragent.client.QWeatherClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherTool implements Tool {

    private final QWeatherClient qWeatherClient;

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
        return ToolType.FIXED;
    }

    @org.springframework.ai.tool.annotation.Tool(name = "weather", description = "根据城市获取天气")
    public String getWeather(String city) {
        try {
            String locationId = qWeatherClient.getLocationId(city);
            if (locationId == null) {
                return "无法查询到该城市：" + city + " 的天气信息，请检查名称是否正确。";
            }

            String weatherNow = qWeatherClient.getNowWeather(locationId);
            String weatherDaily = qWeatherClient.getDailyForecast(locationId);

            return String.format(
                    "【%s天气查询结果】\n实时天气:\n%s\n\n今日预报:\n%s\n\n",
                    city, weatherNow, weatherDaily);
        } catch (Exception e) {
            log.error("查询天气失败", e);
            return "调用和风天气API失败，目前暂时无法提供该城市的天气。请稍后再试。";
        }
    }
}
