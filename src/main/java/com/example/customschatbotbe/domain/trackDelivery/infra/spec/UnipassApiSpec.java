package com.example.customschatbotbe.domain.trackDelivery.infra.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnipassApiSpec {
    public static final String TAG_PROCESS_DETAIL = "cargCsclPrgsInfoDtlQryVo";
    public static final String TAG_PROCESS_DATETIME = "prcsDttm";
    public static final String TAG_PROCESS_STATUS = "cargTrcnRelaBsopTpcd";
    public static final String TAG_PROCESS_COMMENT = "rlbrCn";
    public static final String PARAM_API_KEY = "crkyCn";
    public static final String PARAM_CARGO_NO = "cargMtNo";
    public static final String NA = "N/A";

    public static final DateTimeFormatter UNIPASS_INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter UNIPASS_OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
