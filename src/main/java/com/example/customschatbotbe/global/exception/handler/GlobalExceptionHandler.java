package com.example.customschatbotbe.global.exception.handler;

import com.example.customschatbotbe.global.exception.BusinessException;
import com.example.customschatbotbe.global.exception.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e){
        log.error("BusinessException: ", e);
        ApiResponse<?> apiResponse = ApiResponse.error(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(apiResponse);
    }
}
