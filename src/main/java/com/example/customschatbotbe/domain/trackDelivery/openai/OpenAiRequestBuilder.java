package com.example.customschatbotbe.domain.trackDelivery.openai;


import com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec.SYSTEM_MESSAGE;

/**
 * GPT ChatCompletion 요청을 조립해 주는 헬퍼.
 *  - 함수 스키마 로드·캐시
 *  - system/user/assistant 메시지 리스트 빌드
 *  - 요청 바디(Map)까지 완성
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAiRequestBuilder {
    private static final List<Map<String, Object>> FUNCTION_SCHEMA = loadFunctionSchemaOnce();

    private static List<Map<String, Object>> loadFunctionSchemaOnce() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    new ClassPathResource("functions.json").getInputStream(),
                    new TypeReference<>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException("functions.json 로드 실패", e);
        }
    }

    public static List<Map<String, String>> buildMessages(String userMessage,
                                                          List<Map<String, String>> history) {
        List<Map<String, String>> messages = new ArrayList<>();

        if(history != null){
            boolean hasSystem = history.stream()
                    .anyMatch(msg -> "system".equals(msg.get("role")));
            if (!hasSystem) {
                messages.add(SYSTEM_MESSAGE); // system 메시지가 없을 때만 추가
            }
            messages.addAll(history);
        }else{
            messages.add(SYSTEM_MESSAGE);
        }

        messages.add(Map.of("role", "user", "content", userMessage));

        return messages;
    }

    public static ChatCompletionBodyBuilder builder() {
        return new ChatCompletionBodyBuilder();
    }

    public static class ChatCompletionBodyBuilder {
        private String model = OpenAiApiSpec.GPT_3P5_TURBO;
        private List<Map<String, String>> userMessage;
        private String toolChoice = OpenAiApiSpec.FUNC_AUTO_OPTION;

        public ChatCompletionBodyBuilder model(String model) {
            this.model = model;
            return this;
        }
        public ChatCompletionBodyBuilder userMessage(List<Map<String, String>> userMessage) {
            this.userMessage = userMessage;
            return this;
        }
        public ChatCompletionBodyBuilder toolChoice(String toolChoice) {
            this.toolChoice = toolChoice;
            return this;
        }
        public Map<String, Object> build() {
            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", userMessage);
            body.put("functions", FUNCTION_SCHEMA);
            body.put("function_call", toolChoice);
            return body;
        }
    }
}
