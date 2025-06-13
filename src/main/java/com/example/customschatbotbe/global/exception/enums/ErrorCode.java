package com.example.customschatbotbe.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // TrackDelivery
    INVALID_CARGO_NUMBER_MESSAGE(HttpStatus.BAD_REQUEST, "TRACK-DELIVERY-001", "화물 번호 형식이 올바르지 않습니다."),
    NO_PROGRESS_INFO_MESSAGE(HttpStatus.NOT_FOUND, "TRACK-DELIVERY-002", "조회된 통관 진행 정보가 없습니다."),
    FETCH_ERROR_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "TRACK-DELIVERY-003", "통관 정보를 가져오는 중 예기치 못한 오류가 발생했습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
