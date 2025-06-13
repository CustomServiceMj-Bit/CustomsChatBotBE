package com.example.customschatbotbe.domain.trackDelivery.util;

import com.example.customschatbotbe.global.ProgressDetail;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.customschatbotbe.domain.trackDelivery.infra.spec.UnipassApiSpec.*;

public final class UnipassXmlParser {

    public static Optional<List<ProgressDetail>> parseProgress(String xml)
            throws ParserConfigurationException, SAXException, IOException {
        // 문자열로 들어온 XML을 파싱해 DOM 트리로 만듦
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        // process detail 태그 추출
        NodeList nodeList = doc.getElementsByTagName(TAG_PROCESS_DETAIL);

        List<ProgressDetail> progressList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            // 날짜, 상태, 설명 태그 추출
            Element element = (Element) nodeList.item(i);
            String ProcessDatetime = getTagValue(TAG_PROCESS_DATETIME, element);
            String status = Optional.ofNullable(getTagValue(TAG_PROCESS_STATUS, element)).orElse(NA);
            String comment = Optional.ofNullable(getTagValue(TAG_PROCESS_COMMENT, element)).orElse("");

            // 날짜 포맷 변환
            String datetime;
            try {
                if (ProcessDatetime != null && ProcessDatetime.length() == 14) {
                    LocalDateTime dt = LocalDateTime.parse(ProcessDatetime, UNIPASS_INPUT_FORMATTER);
                    datetime = dt.format(UNIPASS_OUTPUT_FORMATTER);
                } else {
                    datetime = ProcessDatetime != null ? ProcessDatetime : NA;
                }
            } catch (Exception e) {
                datetime = ProcessDatetime;
            }

            progressList.add(new ProgressDetail(datetime, status, comment));
        }

        progressList = progressList.stream()
                .sorted(Comparator.comparing(ProgressDetail::datetime, Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

        progressList.sort(Comparator.comparing(ProgressDetail::datetime, Comparator.nullsLast(String::compareTo)));

        return Optional.of(progressList);
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node != null ? node.getTextContent() : null;
        }
        return null;
    }
}
