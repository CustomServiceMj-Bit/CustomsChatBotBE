package com.example.customschatbotbe.domain.trackDelivery.infra;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.util.UnipassXmlParser;
import com.example.customschatbotbe.global.ProgressDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.UnipassApiSpec.*;
import static com.example.customschatbotbe.global.ErrorMessages.*;

@Component
public class UnipassCargoApiClient {



    @Value("${unipass.api-key}")
    private String apiKey;

    @Value("${unipass.api-url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    public CargoProgressResult getCargoProgressDetails(String cargoMtNo) {
        cargoMtNo = formatCargoNumber(cargoMtNo);
        if (!isValidCargoNumber(cargoMtNo)) {
            return errorResult(INVALID_CARGO_NUMBER_MESSAGE);
        }

        try {
            String url = buildRequestUrl(cargoMtNo);
            String xml = fetchXml(url);

            Optional<List<ProgressDetail>> parsed = UnipassXmlParser.parseProgress(xml);

            return parsed.map(this::successResult)
                    .orElseGet(() -> errorResult(NO_PROGRESS_INFO_MESSAGE));
        } catch (RuntimeException e) {
            return errorResult(FETCH_ERROR_MESSAGE);
        } catch (Exception e) {
            return errorResult(PROCESSING_ERROR_MESSAGE);
        }
    }
    private CargoProgressResult errorResult(String reason) {
        return CargoProgressResult.builder()
                .success(false)
                .errorReason(reason)
                .build();
    }
    private CargoProgressResult successResult(List<ProgressDetail> details) {
        return CargoProgressResult.builder()
                .success(true)
                .progressDetails(details)
                .build();
    }
    private boolean isValidCargoNumber(String cargoMtNo) {
        return CARGO_NO_PATTERN.matcher(cargoMtNo).matches();
    }
    private String buildRequestUrl(String cargoMtNo) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam(PARAM_API_KEY, apiKey)
                .queryParam(PARAM_CARGO_NO, cargoMtNo)
                .build()
                .toUriString();
    }
    private String fetchXml(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("외부 API 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }
    private String formatCargoNumber(String cargoMtNo) {
        return cargoMtNo.replace("-", "").toUpperCase();
    }
}
