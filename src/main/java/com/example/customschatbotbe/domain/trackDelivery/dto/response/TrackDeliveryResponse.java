package com.example.customschatbotbe.domain.trackDelivery.dto.response;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.global.ProgressDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TrackDeliveryResponse {
    private String reply;
    private final Boolean success;
    private final List<ProgressDetail> progressDetails;
    private final String errorReason;
    public static TrackDeliveryResponse fromChatResponse(String reply){
        return TrackDeliveryResponse.builder()
                .reply(reply)
                .build();
    }
    public static TrackDeliveryResponse fromCargoProgressResult(CargoProgressResult cargoProgressResult){
        return TrackDeliveryResponse.builder()
                .success(cargoProgressResult.isSuccess())
                .errorReason(cargoProgressResult.getErrorReason())
                .progressDetails(cargoProgressResult.getProgressDetails())
                .build();
    }
}
