package com.example.customschatbotbe.domain.trackDelivery.openai;

import com.example.customschatbotbe.domain.trackDelivery.infra.UnipassCargoApiClient;
import com.example.customschatbotbe.global.ProgressDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GPT가 요청한 펑션콜링을 처리하고 반환값으로 다시 응답을 받아오는 역할
 *  - gpt가 실행 요청한 함수 실행
 *  - 이전 응답과 함수 결과를 더해 새로운 응답 반환
 */
@RequiredArgsConstructor
@Component
public class FunctionCallProcessor {
    private final OpenAiClient openAiClient;
    private final UnipassCargoApiClient unipassCargoApiClient;

    public String handleFunctionCall(Map<String, Object> gptMessage,
                                      List<Map<String, String>> initialMessages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> functionCall = (Map<String, Object>) gptMessage.get("function_call");
            String functionName   = (String) functionCall.get("name");
            String arguments  = (String) functionCall.get("arguments");
            Map<String, String> args = mapper.readValue(arguments, new TypeReference<>() {});
            String trackingNo = args.get("tracking_number");

            List<ProgressDetail> result = unipassCargoApiClient.getCargoProgressDetails(trackingNo);

            Map<String, String> functionMsg = new HashMap<>();
            functionMsg.put("role", "function");
            functionMsg.put("name", functionName);
            functionMsg.put("content", mapper.writeValueAsString(result));

            List<Map<String, String>> secondMessages = new ArrayList<>(initialMessages);
            secondMessages.add(functionMsg);

            Map<String, Object> requestBody = OpenAiRequestBuilder.builder()
                    .model("gpt-3.5-turbo")
                    .userMessage(secondMessages)
                    .build();

            Map<String, Object> gptResponse = openAiClient.chatCompletion(requestBody);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> finalGptMessage = (Map<String, Object>) choices.get(0).get("message");
                return (String) finalGptMessage.get("content");
            }else{
                throw new RuntimeException("GPT 응답 파싱 실패");
            }
        } catch (Exception e) {
            throw new RuntimeException("함수 실행 중 오류");
        }
    }
}
