package com.example.customschatbotbe.domain.trackDelivery.util;

import com.example.customschatbotbe.global.exception.BusinessException;

import java.util.List;
import java.util.Map;

import static com.example.customschatbotbe.global.exception.enums.ErrorCode.FETCH_ERROR_MESSAGE;

/**
 * GPT 응답에서 첫 번째 메시지를 추출하는 유틸리티 클래스입니다.
 * 응답 구조가 다르거나 비어있을 경우 BusinessException을 던집니다.
 */
public class GptResponseParser {
    public static Map<String, Object> extractMessage(Map<String, Object> gptResponse) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");
        if (choices == null || choices.isEmpty()) {
            // GPT 응답이 비어있는 경우
            throw new BusinessException(FETCH_ERROR_MESSAGE);
        }

        return  (Map<String, Object>) choices.get(0).get("message");
    }
}
