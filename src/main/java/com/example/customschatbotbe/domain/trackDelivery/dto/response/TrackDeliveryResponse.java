package com.example.customschatbotbe.domain.trackDelivery.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackDeliveryResponse {
    private String reply;
    public static TrackDeliveryResponse fromChatResponse(String reply){
        return TrackDeliveryResponse.builder()
                .reply(reply)
                .build();
    }
}
