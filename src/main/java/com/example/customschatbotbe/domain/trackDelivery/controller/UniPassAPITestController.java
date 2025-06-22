package com.example.customschatbotbe.domain.trackDelivery.controller;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.infra.UnipassCargoApiClient;
import com.example.customschatbotbe.global.exception.enums.SuccessCode;
import com.example.customschatbotbe.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("api/test")
//@RequiredArgsConstructor
public class UniPassAPITestController {
//    private final UnipassCargoApiClient unipassCargoApiClient;
//
//    @GetMapping("/cargoNo")
//    public ResponseEntity<ApiResponse<CargoProgressResult>> getCargoResultByCargoNo(@RequestParam String cargoNo){
//        CargoProgressResult cargoProgressResult = unipassCargoApiClient.getCargoProgressDetails(cargoNo);
//        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS, cargoProgressResult));
//    }
//
//    @GetMapping("/blNo")
//    public ResponseEntity<ApiResponse<CargoProgressResult>> getCargoResultByBLNo(@RequestParam String mBlNo,
//                                                                                 @RequestParam String hBlNo,
//                                                                                 @RequestParam String blYear){
//        CargoProgressResult cargoProgressResult = unipassCargoApiClient.getCargoProgressDetails(hBlNo,mBlNo,blYear);
//        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS, cargoProgressResult));
//    }
}
