package org.jragent.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jragent.model.vo.weather.WeatherDailyResponse;
import org.jragent.model.vo.weather.WeatherNowResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Slf4j
public class WeatherClient {

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    private final String apiKey;

    private final String geoApiUrl;

    private final String weatherApiUrl;

    private final String forecastApiUrl;

    public WeatherClient(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${weather.api-key}") String apiKey,
            @Value("${weather.geo-api-url}") String geoApiUrl,
            @Value("${weather.weather-api-url}") String weatherApiUrl,
            @Value("${weather.forecast-api-url}") String forecastApiUrl) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.geoApiUrl = geoApiUrl;
        this.weatherApiUrl = weatherApiUrl;
        this.forecastApiUrl = forecastApiUrl;
    }

    public String getLocationId(String locationName) {
        locationName = locationName.trim().replaceAll("[\r\n\t\u3000]", "");

        URI uri = UriComponentsBuilder.fromUriString(geoApiUrl)
                .queryParam("lang", "zh")
                .queryParam("location", locationName)
                .build()
                .encode()
                .toUri();

        log.info("当前locationName:{}", locationName);
        log.info("当前uri:{}", uri);


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
            validateQWeatherCode(root, "城市查询");
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
            throw new IllegalStateException("解析城市查询结果失败", e);
        }
    }

    public WeatherNowResponse getNowWeather(String locationId) {
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
            validateQWeatherCode(root, "实时天气查询");
            JsonNode now = root.path("now");

            if (now.isMissingNode() || now.isEmpty()) {
                throw new IllegalArgumentException("未找到匹配实时天气数据: " + locationId);
            }

            return WeatherNowResponse.builder()
                    .obsTime(now.path("obsTime").asText())
                    .temp(now.path("temp").asText())
                    .feelsLike(now.path("feelsLike").asText())
                    .text(now.path("text").asText())
                    .windDir(now.path("windDir").asText())
                    .windScale(now.path("windScale").asText())
                    .humidity(now.path("humidity").asText())
                    .precip(now.path("precip").asText())
                    .pressure(now.path("pressure").asText())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("解析实时天气查询结果失败", e);
        }
    }

    public WeatherDailyResponse getTodayForecast(String locationId) {
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
            validateQWeatherCode(root, "天气预报查询");
            JsonNode daily = root.path("daily");

            if (!daily.isArray() || daily.isEmpty()) {
                throw new IllegalArgumentException("未找到匹配天气预报数据: " + locationId);
            }

            JsonNode today = daily.get(0);
            return WeatherDailyResponse.builder()
                    .fxDate(today.path("fxDate").asText())
                    .sunrise(today.path("sunrise").asText())
                    .sunset(today.path("sunset").asText())
                    .textDay(today.path("textDay").asText())
                    .textNight(today.path("textNight").asText())
                    .tempMax(today.path("tempMax").asText())
                    .tempMin(today.path("tempMin").asText())
                    .uvIndex(today.path("uvIndex").asText())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("解析三日天气预报查询结果失败", e);
        }
    }

    private void validateQWeatherCode(JsonNode root, String scene) {
        String code = root.path("code").asText();
        if (!"200".equals(code)) {
            throw new IllegalStateException(scene + "失败, qweather code=" + code);
        }
    }
}
