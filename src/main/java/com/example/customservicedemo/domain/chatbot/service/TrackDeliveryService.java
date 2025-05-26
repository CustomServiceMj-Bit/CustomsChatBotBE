package com.example.customservicedemo.domain.chatbot.service;

import com.example.customservicedemo.global.ProgressDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackDeliveryService {

    private static final String TAG_PROCESS_DETAIL = "cargCsclPrgsInfoDtlQryVo";
    private static final String TAG_PROCESS_DATETIME = "prcsDttm";
    private static final String TAG_PROCESS_STATUS = "cargTrcnRelaBsopTpcd";
    private static final String TAG_PROCESS_COMMENT = "rlbrCn";
    private static final String NA = "N/A";
    private static final String PARAM_API_KEY = "crkyCn";
    private static final String PARAM_CARGO_NO = "cargMtNo";
    private static final DateTimeFormatter UNIPASS_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter UNIPASS_OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${unipass.api-key}")
    private String apiKey;

    @Value("${unipass.api-url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    public List<ProgressDetail> getCargoProgressDetails(String cargoMtNo) {
        try {
            String url   = buildRequestUrl(cargoMtNo);
            String xml   = fetchXml(url);

            return parseProgress(xml);
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

    private List<ProgressDetail> parseProgress(String xml) throws Exception {
        // 문자열로 들어온 XML을 파싱해 DOM 트리로 만듦
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        // process detail 태그 추출
        NodeList nodeList = doc.getElementsByTagName(TAG_PROCESS_DETAIL);

        // 통관 정보가 없는 경우
        if (nodeList.getLength() == 0) {
            throw new RuntimeException("통관 진행 상세 정보를 찾을 수 없습니다.");
        }

        List<ProgressDetail> progressList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            // 날짜, 상태, 설명 태그 추출
            Element element = (Element) nodeList.item(i);
            String prosDtm = getTagValue(TAG_PROCESS_DATETIME, element);
            String status = Optional.ofNullable(getTagValue(TAG_PROCESS_STATUS, element)).orElse(NA);
            String comment = Optional.ofNullable(getTagValue(TAG_PROCESS_COMMENT, element)).orElse("");

            // 날짜 포맷 변환
            String datetime;
            try {
                if (prosDtm != null && prosDtm.length() == 14) {
                    LocalDateTime dt = LocalDateTime.parse(prosDtm, UNIPASS_INPUT_FORMATTER);
                    datetime = dt.format(UNIPASS_OUTPUT_FORMATTER);
                } else {
                    datetime = prosDtm != null ? prosDtm : NA;
                }
            } catch (Exception e) {
                datetime = prosDtm != null ? prosDtm : NA;
            }

            progressList.add(new ProgressDetail(datetime, status, comment));
        }

        progressList = progressList.stream()
                .sorted(Comparator.comparing(ProgressDetail::datetime, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

        return progressList;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node != null ? node.getTextContent() : null;
        }
        return null;
    }
}
