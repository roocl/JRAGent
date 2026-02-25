package org.jragent.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jragent.exception.BaseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class QWeatherClient {

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    private final String apiKey;

    private final String geoApiUrl;

    private final String weatherApiUrl;

    private final String forecastApiUrl;

    public QWeatherClient(
            RestClient.Builder restClientBuilder,
            @Value("${qweather.api-key}") String apiKey,
            @Value("${qweather.geo-api-url}") String geoApiUrl,
            @Value("${qweather.weather-api-url}") String weatherApiUrl,
            @Value("${qweather.forecast-api-url}") String forecastApiUrl) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
        this.geoApiUrl = geoApiUrl;
        this.weatherApiUrl = weatherApiUrl;
        this.forecastApiUrl = forecastApiUrl;
    }

    public String getLocationId(String locationName) {
        URI uri = UriComponentsBuilder.fromUriString(geoApiUrl)
                .queryParam("lang", "zh")
                .queryParam("location", locationName)
                .build(true)
                .toUri();

        String response = restClient.get()
                .uri(uri)
                .header("X-QW-Api-Key", apiKey)
                .retrieve()
                .body(String.class);

        if (response == null || response.isBlank()) {
            throw new IllegalStateException("城市查询返回为空");
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode locationList = root.path("location");

            if (!locationList.isArray() || locationList.isEmpty()) {
                throw new IllegalArgumentException("未找到匹配城市: " + locationName);
            }

            JsonNode firstLocation = locationList.get(0);
            String locationId = firstLocation.get("id").asText();

            if (locationId == null || locationId.isBlank()) {
                throw new IllegalStateException("城市查询结果缺少locationId");
            }

            return locationId;
        } catch (Exception e) {
            throw new BaseException("解析城市查询结果失败: " + e);
        }
    }

    public String getNowWeather(String locationId) {
        URI uri = UriComponentsBuilder.fromUriString(weatherApiUrl)
                .queryParam("location", locationId)
                .build(true)
                .toUri();

        String response = restClient.get()
                .uri(uri)
                .header("X-QW-Api-Key", apiKey)
                .retrieve()
                .body(String.class);

        if (response == null || response.isBlank()) {
            throw new IllegalStateException("实时天气查询返回为空");
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode now = root.path("now");

            if (now.isEmpty()) {
                throw new IllegalArgumentException("未找到匹配实时天气数据: " + locationId);
            }

            return now.asText();
        } catch (Exception e) {
            throw new BaseException("解析实时天气查询结果失败: " + e);
        }
    }

    public String getDailyForecast(String locationId) {
        URI uri = UriComponentsBuilder.fromUriString(forecastApiUrl)
                .queryParam("location", locationId)
                .build(true)
                .toUri();

        String response = restClient.get()
                .uri(uri)
                .header("X-QW-Api-Key", apiKey)
                .retrieve()
                .body(String.class);

        if (response == null || response.isBlank()) {
            throw new IllegalStateException("三日天气预报查询返回为空");
        }

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode daily = root.path("daily");

            if (daily.isEmpty()) {
                throw new IllegalArgumentException("未找到匹配天气预报数据: " + locationId);
            }

            return daily.asText();
        } catch (Exception e) {
            throw new BaseException("解析三日天气预报查询结果失败: " + e);
        }
    }
}
