package com.example.customschatbotbe.domain.chatbot.service;

import com.example.customschatbotbe.domain.chatbot.dto.request.AiChatRequest;
import com.example.customschatbotbe.domain.chatbot.dto.request.ChatRequest;
import com.example.customschatbotbe.domain.chatbot.dto.response.ChatResponse;
import com.example.customschatbotbe.global.exception.BusinessException;
import com.example.customschatbotbe.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatBotService {
    private final WebClient webClient;

    public Mono<ChatResponse> generateReply(ChatRequest chatRequest, String sessionId){
        try {
            return webClient.post()
                    .uri("/predict")
                    .bodyValue(AiChatRequest.builder()
                            .message(chatRequest.getMessage())
                            .sessionId(sessionId)
                            .build())
                    .retrieve()
                    .bodyToMono(ChatResponse.class);
        } catch (Exception e) {
            System.out.println("AI 서버 호출 실패"+e);
            throw new BusinessException(ErrorCode.FETCH_ERROR_MESSAGE);
        }

    }
}
