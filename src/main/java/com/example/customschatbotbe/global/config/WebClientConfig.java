package com.example.customschatbotbe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${ai.url}")
    private String aiUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.create(aiUrl);
    }
}
