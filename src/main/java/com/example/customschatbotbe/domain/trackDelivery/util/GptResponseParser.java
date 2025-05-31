package com.example.customschatbotbe.domain.trackDelivery.util;

import java.util.List;
import java.util.Map;

public class GptResponseParser {
    public static Map<String, Object> extractMessage(Map<String, Object> gptResponse) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("GPT 응답 파싱 실패: choices 비어 있음");
        }

        return  (Map<String, Object>) choices.get(0).get("message");
    }
}
