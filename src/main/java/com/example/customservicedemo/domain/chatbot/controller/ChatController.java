package com.example.customservicedemo.domain.chatbot.controller;

import com.example.customservicedemo.domain.chatbot.dto.request.ChatRequest;
import com.example.customservicedemo.domain.chatbot.dto.response.ChatResponse;
import com.example.customservicedemo.domain.chatbot.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenAiService openAiService;
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest){
        ChatResponse chatResponse = openAiService.askToGpt(chatRequest);
        return ResponseEntity.ok(chatResponse);
    }
}
