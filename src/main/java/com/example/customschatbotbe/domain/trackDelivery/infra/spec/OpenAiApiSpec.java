package com.example.customschatbotbe.domain.trackDelivery.infra.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAiApiSpec {
    public static final Map<String, String> SYSTEM_MESSAGE = Map.of(
            "role", "system",
            "content", "너는 통관진행조회 도우미야. 운송장 번호를 이해하고 응답해줘."
    );
    public static final String GPT_3P5_TURBO = "gpt-3.5-turbo";
    public static final String FUNC_AUTO_OPTION = "auto";
    public static final String FUNC_KEY = "function_call";
}