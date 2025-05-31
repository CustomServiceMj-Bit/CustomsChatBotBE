package com.example.customschatbotbe.domain.trackDelivery.dto;

import com.example.customschatbotbe.global.ProgressDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CargoProgressResult {
    private final boolean success;
    private final List<ProgressDetail> progressDetails;
    private final String errorReason;
}