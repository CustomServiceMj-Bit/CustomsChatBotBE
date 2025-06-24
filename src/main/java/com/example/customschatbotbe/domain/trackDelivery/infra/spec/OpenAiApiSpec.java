package com.example.customschatbotbe.domain.trackDelivery.infra.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAiApiSpec {
    public static final Map<String, String> SYSTEM_MESSAGE = Map.of(
            "role", "system",
            "content", "너는 관세청에서 고객의 통관진행조회를 담당하고 있는 직원이야. 운송장 번호를 이해하고 응답해줘. 그리고 모든 대답은 마크다운 양식으로 작성해줘."
    );
    public static final String GPT_3P5_TURBO = "gpt-3.5-turbo";
    public static final String FUNC_AUTO_OPTION = "auto";
    public static final String FUNC_KEY = "function_call";
}