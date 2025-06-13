package com.example.customschatbotbe.domain.trackDelivery.controller;

import com.example.customschatbotbe.domain.trackDelivery.dto.request.TrackDeliveryRequest;
import com.example.customschatbotbe.domain.trackDelivery.dto.response.TrackDeliveryResponse;
import com.example.customschatbotbe.domain.trackDelivery.service.TrackDeliveryChatService;
import com.example.customschatbotbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.customschatbotbe.global.exception.enums.SuccessCode.SUCCESS;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class TrackDeliveryController {
    private final TrackDeliveryChatService trackDeliveryChatService;
    @PostMapping
    public ResponseEntity<ApiResponse<TrackDeliveryResponse>> chat(@RequestBody TrackDeliveryRequest trackDeliveryRequest){
        TrackDeliveryResponse trackDeliveryResponse = trackDeliveryChatService.askToGpt(trackDeliveryRequest);
        return ResponseEntity.ok(ApiResponse.success(SUCCESS, trackDeliveryResponse));
    }
}
