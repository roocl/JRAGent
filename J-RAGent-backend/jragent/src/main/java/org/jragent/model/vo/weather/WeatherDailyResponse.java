package org.jragent.model.vo.weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "WeatherDailyResponse", description = "今日天气预报响应")
public class WeatherDailyResponse {
    @Schema(description = "预报日期", example = "2026-02-25")
    private String fxDate;

    @Schema(description = "日出时间", example = "06:55")
    private String sunrise;

    @Schema(description = "日落时间", example = "18:02")
    private String sunset;

    @Schema(description = "白天天气", example = "多云")
    private String textDay;

    @Schema(description = "夜间天气", example = "雨夹雪")
    private String textNight;

    @Schema(description = "最高温度(℃)", example = "7")
    private String tempMax;

    @Schema(description = "最低温度(℃)", example = "2")
    private String tempMin;

    @Schema(description = "紫外线指数", example = "1")
    private String uvIndex;
}
