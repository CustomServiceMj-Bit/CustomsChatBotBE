package com.example.customschatbotbe.domain.trackDelivery.openai;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.infra.UnipassCargoApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.OpenAiApiSpec.FUNC_KEY;
import static com.example.customschatbotbe.global.exception.enums.ErrorCode.FETCH_ERROR_MESSAGE;

/**
 * GPT가 요청한 펑션콜링을 처리하고 반환값으로 다시 응답을 받아오는 역할
 *  - gpt가 실행 요청한 함수 실행
 *  - 이전 응답과 함수 결과를 더해 새로운 응답 반환
 */
@RequiredArgsConstructor
@Component
public class FunctionCallProcessor {
    private final UnipassCargoApiClient unipassCargoApiClient;

    public CargoProgressResult handleFunctionCall(Map<String, Object> gptMessage) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> functionCall = (Map<String, Object>) gptMessage.get(FUNC_KEY);
            String functionName = (String) functionCall.get("name");
            String arguments = (String) functionCall.get("arguments");
            Map<String, String> args = mapper.readValue(arguments, new TypeReference<>() {});
            CargoProgressResult result;
            System.out.println(functionName);
            switch (functionName) {
                case "trackByCargoMtNo" -> {
                    String trackingNo = args.get("cargoMtNo");
                    result = unipassCargoApiClient.getCargoProgressDetails(trackingNo);
                }
                case "trackByBlInfo" -> {
                    String hblNo = args.get("hBlNo");
                    String mblNo = args.get("mBlNo");
                    String year = args.get("year");
                    result = unipassCargoApiClient.getCargoProgressDetails(hblNo, mblNo, year);
                }
                default -> result = CargoProgressResult.builder()
                        .success(false)
                        .errorReason(FETCH_ERROR_MESSAGE.getMessage())
                        .build();
            }
            return result;
        } catch (Exception e) {
            return CargoProgressResult.builder()
                    .success(false)
                    .errorReason(FETCH_ERROR_MESSAGE.getMessage())
                    .build();
        }
    }
}
