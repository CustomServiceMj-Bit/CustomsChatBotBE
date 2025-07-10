package com.example.customschatbotbe.domain.dto.response;

import com.example.customschatbotbe.global.ProgressDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatResponse {
    private String reply;
    private final Boolean success;
    private final List<ProgressDetail> progressDetails;
    private final String errorReason;
}
