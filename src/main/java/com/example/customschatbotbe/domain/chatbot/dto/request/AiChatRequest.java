package com.example.customschatbotbe.domain.chatbot.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AiChatRequest {
    private String message;
    private String sessionId;
}
