package com.example.customservicedemo.domain.chatbot.service;

import com.example.customservicedemo.domain.chatbot.dto.request.ChatRequest;
import com.example.customservicedemo.domain.chatbot.dto.response.ChatResponse;
import com.example.customservicedemo.global.ProgressDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final WebClient webClient = WebClient.create();

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.api-url}")
    private String apiUrl;

    private final TrackDeliveryService trackDeliveryService;

    private List<Map<String, Object>> functionSchema;

    private static final Map<String, String> SYSTEM_MESSAGE = Map.of(
            "role", "system",
            "content", "너는 배송조회 전문 AI야. 운송장 번호를 이해하고 응답해줘."
    );

    @PostConstruct
    private void loadFunctionSchema() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.functionSchema = mapper.readValue(
                new ClassPathResource("function.json").getInputStream(),
                new TypeReference<>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException("functions.json 로드 실패", e);
        }
    }

    public ChatResponse askToGpt(ChatRequest chatRequest){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = buildMessages(chatRequest.getMessage());
        requestBody.put("messages", messages);
        requestBody.put("functions", functionSchema);
        requestBody.put("function_call", "auto");

        Map firstResp = sendChatCompletion(requestBody);

        String reply = "";

        List<Map<String, Object>> choices = (List<Map<String, Object>>) firstResp.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            if (message.containsKey("function_call")) {
                reply = handleFunctionCall(message, messages);
            } else {
                reply = (String) message.get("content");
            }
        } else {
            reply = "GPT 응답을 파싱할 수 없습니다.";
        }

        return ChatResponse.fromChatResponse(reply);
    }

    private List<Map<String, String>> buildMessages(String userMessage){
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(SYSTEM_MESSAGE);
        messages.add(Map.of(
                "role", "user",
                "content", userMessage
        ));
        return messages;
    }

    private Map sendChatCompletion(Map<String, Object> body) {
        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    private String handleFunctionCall(Map<String, Object> message,
                                      List<Map<String, String>> initialMessages) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> functionCall = (Map<String, Object>) message.get("function_call");
            String functionName   = (String) functionCall.get("name");
            String argumentsJson  = (String) functionCall.get("arguments");
            Map<String, String> args = mapper.readValue(argumentsJson, new TypeReference<>() {});
            String trackingNo = args.get("tracking_number");

            List<ProgressDetail> result = trackDeliveryService.getCargoProgressDetails(trackingNo);

            Map<String, Object> functionMsg = new HashMap<>();
            functionMsg.put("role", "function");
            functionMsg.put("name", functionName);
            functionMsg.put("content", mapper.writeValueAsString(result));

            List<Object> secondMessages = new ArrayList<>(initialMessages);
            secondMessages.add(functionMsg);

            Map<String, Object> secondReq = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", secondMessages
            );

            Map secondResp = sendChatCompletion(secondReq);
            List<Map<String, Object>> secondChoices = (List<Map<String, Object>>) secondResp.get("choices");
            if (secondChoices != null && !secondChoices.isEmpty()) {
                Map<String, Object> finalMsg = (Map<String, Object>) secondChoices.get(0).get("message");
                return (String) finalMsg.get("content");
            }
            return "GPT 최종 응답을 파싱할 수 없습니다.";
        } catch (Exception e) {
            return "함수 실행 중 오류: " + e.getMessage();
        }
    }
}
