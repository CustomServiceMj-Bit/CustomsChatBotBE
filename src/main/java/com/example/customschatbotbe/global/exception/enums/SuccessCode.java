package com.example.customschatbotbe.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    SUCCESS(HttpStatus.OK, "SUCCESS", "요청에 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String successCode;
    private final String message;
}
