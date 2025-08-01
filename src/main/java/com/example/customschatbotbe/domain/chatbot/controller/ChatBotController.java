package com.example.customschatbotbe.domain.chatbot.controller;

import com.example.customschatbotbe.domain.chatbot.service.ChatBotService;
import com.example.customschatbotbe.domain.chatbot.dto.request.ChatRequest;
import com.example.customschatbotbe.domain.chatbot.dto.response.ChatResponse;
import com.example.customschatbotbe.global.exception.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.example.customschatbotbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<ChatResponse>>> chat(@RequestBody ChatRequest chatRequest, HttpServletRequest httpServletRequest) {
        String sessionId = httpServletRequest.getSession().getId();
        System.out.println("sessionId: "+sessionId);
        return chatBotService.generateReply(chatRequest, sessionId)
                .map(chatResponse -> ResponseEntity.ok(ApiResponse.success(SUCCESS, chatResponse)));
    }
}
