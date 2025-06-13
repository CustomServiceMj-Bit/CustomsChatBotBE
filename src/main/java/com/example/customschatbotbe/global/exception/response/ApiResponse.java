package com.example.customschatbotbe.global.exception.response;

import com.example.customschatbotbe.global.exception.enums.ErrorCode;
import com.example.customschatbotbe.global.exception.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private HttpStatus status;
    private int httpStatusCode;
    private String responseCode;
    private String resultMsg;
    private T data;

//    public static <T> ApiResponse<T> success(final SuccessCode successCode) {
//        return ApiResponse.<T>builder()
//                .status(successCode.getHttpStatus())
//                .httpStatusCode(successCode.getHttpStatus().value())
//                .responseCode(successCode.getSuccessCode())
//                .resultMsg(successCode.getMessage())
//                .build();
//    }

    public static <T> ApiResponse<T> success(final SuccessCode successCode, final T data) {
        return ApiResponse.<T>builder()
                .status(successCode.getHttpStatus())
                .httpStatusCode(successCode.getHttpStatus().value())
                .responseCode(successCode.getSuccessCode())
                .resultMsg(successCode.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse<?> error(final ErrorCode errorCode) {
        return ApiResponse.builder()
                .status(errorCode.getHttpStatus())
                .httpStatusCode(errorCode.getHttpStatus().value())
                .responseCode(errorCode.getErrorCode())
                .resultMsg(errorCode.getMessage())
                .build();
    }
}
