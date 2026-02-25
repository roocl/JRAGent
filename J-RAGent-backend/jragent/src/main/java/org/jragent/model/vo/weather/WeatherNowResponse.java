package org.jragent.model.vo.weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "WeatherNowResponse", description = "实时天气响应")
public class WeatherNowResponse {
    @Schema(description = "观测时间", example = "2026-02-25T19:24+08:00")
    private String obsTime;

    @Schema(description = "当前温度(℃)", example = "6")
    private String temp;

    @Schema(description = "体感温度(℃)", example = "3")
    private String feelsLike;

    @Schema(description = "天气现象", example = "雾")
    private String text;

    @Schema(description = "风向", example = "东北风")
    private String windDir;

    @Schema(description = "风力等级", example = "2")
    private String windScale;

    @Schema(description = "相对湿度(%)", example = "70")
    private String humidity;

    @Schema(description = "降水量(mm)", example = "0.0")
    private String precip;

    @Schema(description = "气压(hPa)", example = "1016")
    private String pressure;
}
