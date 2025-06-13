package com.example.customschatbotbe.domain.trackDelivery.openai;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.infra.UnipassCargoApiClient;
import com.example.customschatbotbe.domain.trackDelivery.util.GptResponseParser;
import com.example.customschatbotbe.global.exception.BusinessException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec.FUNC_KEY;
import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec.GPT_3P5_TURBO;
import static com.example.customschatbotbe.global.exception.enums.ErrorCode.FETCH_ERROR_MESSAGE;

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
            Map<String, Object> functionCall = (Map<String, Object>) gptMessage.get(FUNC_KEY);
            String functionName = (String) functionCall.get("name");
            String arguments = (String) functionCall.get("arguments");
            Map<String, String> args = mapper.readValue(arguments, new TypeReference<>() {});
            String trackingNo = args.get("tracking_number");

            CargoProgressResult result = unipassCargoApiClient.getCargoProgressDetails(trackingNo);

            Map<String, String> functionMsg = new HashMap<>();
            functionMsg.put("role", "function");
            functionMsg.put("name", functionName);
            functionMsg.put("content", mapper.writeValueAsString(result));

            List<Map<String, String>> secondMessages = new ArrayList<>(initialMessages);
            secondMessages.add(functionMsg);

            Map<String, Object> requestBody = GptMessageFactory.builder()
                    .model(GPT_3P5_TURBO)
                    .userMessage(secondMessages)
                    .build();

            Map<String, Object> gptResponse = openAiClient.chatCompletion(requestBody);
            Map<String, Object> finalGptMessage = GptResponseParser.extractMessage(gptResponse);
            return (String) finalGptMessage.get("content");
        } catch (Exception e) {
            throw new BusinessException(FETCH_ERROR_MESSAGE);
        }
    }
}
