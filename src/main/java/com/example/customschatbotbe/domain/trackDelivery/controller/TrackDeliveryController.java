package com.example.customschatbotbe.domain.trackDelivery.controller;

import com.example.customschatbotbe.domain.trackDelivery.dto.request.TrackDeliveryRequest;
import com.example.customschatbotbe.domain.trackDelivery.dto.response.TrackDeliveryResponse;
import com.example.customschatbotbe.domain.trackDelivery.service.TrackDeliveryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class TrackDeliveryController {
    private final TrackDeliveryChatService trackDeliveryChatService;
    @PostMapping
    public ResponseEntity<TrackDeliveryResponse> chat(@RequestBody TrackDeliveryRequest trackDeliveryRequest){
        TrackDeliveryResponse trackDeliveryResponse = trackDeliveryChatService.askToGpt(trackDeliveryRequest);
        return ResponseEntity.ok(trackDeliveryResponse);
    }
}
