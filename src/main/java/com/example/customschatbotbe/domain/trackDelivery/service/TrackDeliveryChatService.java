package com.example.customschatbotbe.domain.trackDelivery.service;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.openai.FunctionCallProcessor;
import com.example.customschatbotbe.domain.trackDelivery.openai.GptMessageFactory;
import com.example.customschatbotbe.domain.trackDelivery.openai.OpenAiClient;
import com.example.customschatbotbe.domain.trackDelivery.dto.request.TrackDeliveryRequest;
import com.example.customschatbotbe.domain.trackDelivery.dto.response.TrackDeliveryResponse;
import com.example.customschatbotbe.domain.trackDelivery.util.GptResponseParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec.*;

@Service
@RequiredArgsConstructor
public class TrackDeliveryChatService {
    private final OpenAiClient openAiClient;
    private final FunctionCallProcessor functionCallProcessor;

    public TrackDeliveryResponse askToGpt(TrackDeliveryRequest trackDeliveryRequest){
        List<Map<String, String>> userMessage = GptMessageFactory.buildMessages(trackDeliveryRequest.getMessage(), null) ;

        Map<String, Object> requestBody = GptMessageFactory.builder()
                .model(GPT_3P5_TURBO)
                .userMessage(userMessage)
                .toolChoice(FUNC_AUTO_OPTION)
                .build();

        Map<String, Object> gptResponse = openAiClient.chatCompletion(requestBody);

        Map<String, Object> gptMessage = GptResponseParser.extractMessage(gptResponse);
        if (gptMessage.containsKey(FUNC_KEY)) {
            CargoProgressResult cargoProgressResult = functionCallProcessor.handleFunctionCall(gptMessage);
            return TrackDeliveryResponse.fromCargoProgressResult(cargoProgressResult);
        } else {
            String reply = (String) gptMessage.get("content");
            return TrackDeliveryResponse.fromChatResponse(reply);
        }
    }
}
