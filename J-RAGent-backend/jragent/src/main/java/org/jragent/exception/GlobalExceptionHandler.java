package org.jragent.exception;

import lombok.extern.slf4j.Slf4j;
import org.jragent.model.common.ApiResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ApiResponse<Object> handleBizException(BaseException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Object> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("静态资源不存在: {}", e.getResourcePath());
        return new ApiResponse<>(404, "资源不存在", null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求体格式错误: {}", e.getMessage());
        return new ApiResponse<>(400, "请求体格式错误", null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        log.error("服务器内部错误", e);
        return ApiResponse.error("服务器内部错误");
    }
}
