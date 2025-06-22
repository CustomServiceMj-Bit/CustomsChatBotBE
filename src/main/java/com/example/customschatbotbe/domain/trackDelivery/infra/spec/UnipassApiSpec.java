package com.example.customschatbotbe.domain.trackDelivery.infra.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnipassApiSpec {
    public static final String TAG_PROCESS_DETAIL = "cargCsclPrgsInfoDtlQryVo";
    public static final String TAG_PROCESS_DATETIME = "prcsDttm";
    public static final String TAG_PROCESS_STATUS = "cargTrcnRelaBsopTpcd";
    public static final String TAG_PROCESS_COMMENT = "rlbrCn";
    public static final String PARAM_API_KEY = "crkyCn";
    public static final String PARAM_CARGO_NO = "cargMtNo";
    public static final String PARAM_HBL_NO = "hblNo";
    public static final String PARAM_MBL_NO = "mblNo";
    public static final String PARAM_BL_YEAR = "blYy";
    public static final String NA = "N/A";

    public static final DateTimeFormatter UNIPASS_INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter UNIPASS_OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Pattern CARGO_NO_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Z0-9]{15}|(?=.*[A-Z])(?=.*\\d)[A-Z0-9]{19}$");
}
