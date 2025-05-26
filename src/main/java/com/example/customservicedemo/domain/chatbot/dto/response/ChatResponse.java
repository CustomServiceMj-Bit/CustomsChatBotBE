package com.example.customservicedemo.domain.chatbot.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponse {
    private String reply;
    public static ChatResponse fromChatResponse(String reply){
        return ChatResponse.builder()
                .reply(reply)
                .build();
    }
}
