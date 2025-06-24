package com.example.customschatbotbe.domain.trackDelivery.infra;

import com.example.customschatbotbe.domain.trackDelivery.dto.CargoProgressResult;
import com.example.customschatbotbe.domain.trackDelivery.util.UnipassXmlParser;
import com.example.customschatbotbe.global.ProgressDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.UnipassApiSpec.*;
import static com.example.customschatbotbe.global.exception.enums.ErrorCode.*;

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
            return CargoProgressResult.builder()
                    .success(false)
                    .errorReason(INVALID_CARGO_NUMBER_MESSAGE.getMessage())
                    .build();
        }
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(PARAM_CARGO_NO, cargoMtNo);
        return getCargoProgressResult(queryParams);
    }

    public CargoProgressResult getCargoProgressDetails(String hBlNo, String mBlNo, String year){
        System.out.println(hBlNo+" "+mBlNo+" "+year);
        if(hBlNo.equals("") || mBlNo.equals("") || year.equals("")){
            return CargoProgressResult.builder()
                    .success(false)
                    .errorReason("통관 조회를 위해 BL 번호, 연도, 세관 코드를 모두 입력해주세요.")
                    .build();
        }

        if(true){
            // 유효성 조회 메서드 넣기
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(PARAM_HBL_NO, hBlNo);
        queryParams.put(PARAM_MBL_NO, mBlNo);
        queryParams.put(PARAM_BL_YEAR, year);
        return getCargoProgressResult(queryParams);
    }

    private CargoProgressResult getCargoProgressResult(Map<String, String> queryParams) {
        String url = buildRequestUrl(queryParams);
        String xml = fetchXml(url);

        Optional<List<ProgressDetail>> parsed;
        try {
            parsed = UnipassXmlParser.parseProgress(xml);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return CargoProgressResult.builder()
                    .success(false)
                    .errorReason(FETCH_ERROR_MESSAGE.getMessage())
                    .build();
        }

        if (parsed.isPresent() && !parsed.get().isEmpty()) {
            return successResult(parsed.get());
        } else {
            return CargoProgressResult.builder()
                    .success(false)
                    .errorReason(NO_PROGRESS_INFO_MESSAGE.getMessage())
                    .build();
        }
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
    private String buildRequestUrl(Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam(PARAM_API_KEY, apiKey);
        for (Map.Entry<String, String> entry : queryParams.entrySet()){
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.build().toUriString();
    }
    private String fetchXml(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    private String formatCargoNumber(String cargoMtNo) {
        return cargoMtNo.replace("-", "").toUpperCase();
    }
}
