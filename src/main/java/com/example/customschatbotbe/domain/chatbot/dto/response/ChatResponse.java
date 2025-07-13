package com.example.customschatbotbe.domain.chatbot.dto.response;

import com.example.customschatbotbe.global.ProgressDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ChatResponse {
    private String reply;
    private final Boolean success;
    @JsonProperty("progress_details")
    private final List<ProgressDetail> progressDetails;
    @JsonProperty("error_reason")
    private final String errorReason;
}
