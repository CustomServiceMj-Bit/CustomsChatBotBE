package com.example.customschatbotbe.domain.trackDelivery.service;

import com.example.customschatbotbe.domain.trackDelivery.openai.FunctionCallProcessor;
import com.example.customschatbotbe.domain.trackDelivery.openai.OpenAiRequestBuilder;
import com.example.customschatbotbe.domain.trackDelivery.openai.OpenAiClient;
import com.example.customschatbotbe.domain.trackDelivery.dto.request.TrackDeliveryRequest;
import com.example.customschatbotbe.domain.trackDelivery.dto.response.TrackDeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrackDeliveryChatService {
    private final OpenAiClient openAiClient;
    private final FunctionCallProcessor functionCallProcessor;

    public TrackDeliveryResponse askToGpt(TrackDeliveryRequest trackDeliveryRequest){
        List<Map<String, String>> userMessage = OpenAiRequestBuilder.buildMessages(trackDeliveryRequest.getMessage(), null) ;

        Map<String, Object> requestBody = OpenAiRequestBuilder.builder()
                .model("gpt-3.5-turbo")
                .userMessage(userMessage)
                .toolChoice("auto")
                .build();

        Map<String, Object> gptResponse = openAiClient.chatCompletion(requestBody);

        String reply;

        List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> gptMessage = (Map<String, Object>) choices.get(0).get("message");

            if (gptMessage.containsKey("function_call")) {
                reply = functionCallProcessor.handleFunctionCall(gptMessage, userMessage);
            } else {
                reply = (String) gptMessage.get("content");
            }
        } else {
            throw new RuntimeException("GPT 응답 파싱 실패");
        }

        return TrackDeliveryResponse.fromChatResponse(reply);
    }
}
