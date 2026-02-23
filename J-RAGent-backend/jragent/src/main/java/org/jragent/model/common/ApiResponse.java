package org.jragent.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "ApiResponse", description = "Unified API response envelope")
public class ApiResponse<T> {

    @Schema(description = "Business status code", example = "200")
    private int code;

    @Schema(description = "Response message", example = "success")
    private String message;

    @Schema(description = "Response payload")
    private T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(ApiCode.SUCCESS.code, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiCode.SUCCESS.code, ApiCode.SUCCESS.message, data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ApiCode.SUCCESS.code, ApiCode.SUCCESS.message, null);
    }

    public static <T> ApiResponse<T> error(ApiCode code, String message) {
        return new ApiResponse<>(code.getCode(), message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ApiCode.ERROR.getCode(), message, null);
    }

    @Getter
    @AllArgsConstructor
    public enum ApiCode {
        SUCCESS(200, "success"),
        ERROR(500, "error");

        private final int code;
        private final String message;
    }
}
