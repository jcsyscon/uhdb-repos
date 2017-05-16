package com.realsnake.sample.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.realsnake.sample.model.common.api.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {CommonException.class})
    public void handleException(CommonException e) {
        LOGGER.error("================================================================================");
        LOGGER.error("<<Exception>> {}", e.getMessage());
        LOGGER.error("================================================================================");
    }

    @ExceptionHandler(value = {Exception.class})
    public String HandleException(Exception e) {
        LOGGER.error("<<Exception>>", e);

        return "error";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = CommonApiException.class)
    @ResponseBody
    public ApiResponse<?> handleApiException(HttpServletRequest req, CommonApiException e) throws Exception {
        LOGGER.error("================================================================================");
        LOGGER.error("<<API Exception>> 응답코드: {}, 응답메시지: {}, 에러: {}", e.getResCode(), e.getResMessage(), e.getMessage());
        LOGGER.error("================================================================================");

        return new ApiResponse<>(e.getResCode(), e.getResMessage(), e.getMessage());
    }

}
