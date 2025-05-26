package com.example.customschatbotbe.domain.trackDelivery.infra;

import com.example.customschatbotbe.global.ProgressDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.UnipassApiSpec.PARAM_API_KEY;
import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.UnipassApiSpec.PARAM_CARGO_NO;

@Component
public class UnipassCargoApiClient {

    @Value("${unipass.api-key}")
    private String apiKey;

    @Value("${unipass.api-url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    public List<ProgressDetail> getCargoProgressDetails(String cargoMtNo) {
        try {
            String url   = buildRequestUrl(cargoMtNo);
            String xml   = fetchXml(url);

            return UnipassXmlParser.parseProgress(xml);
        } catch (RuntimeException e) {           // our custom wrapper for fetch errors
            return null;
        } catch (Exception e) {                  // parsing error or no record
            return null;
        }
    }

    private String buildRequestUrl(String cargoMtNo) {
        String formatted = cargoMtNo.replace("-", "").toUpperCase();
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam(PARAM_API_KEY, apiKey)
                .queryParam(PARAM_CARGO_NO, formatted)
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
}
