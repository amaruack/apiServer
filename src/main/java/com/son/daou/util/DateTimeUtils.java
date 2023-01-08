package com.son.daou.util;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final String DATE_TIME_ID_REGEX = "20[0-3][0-9]-[0-2][0-9]-[0-3][0-9]T[0-5][0-9]";

    public static final String ZONE_NAME = "Asia/Seoul";
    public static final ZoneId ZONE = ZoneId.of(ZONE_NAME);
    public static final String LOCAL_OFFSET_ID = "+09:00";
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.of(LOCAL_OFFSET_ID);

    public static final String DATE_TIME_ID_PATTERN = "yyyy-MM-dd'T'HH";
    public static final String READ_FILE_DATE_TIME_PATTERN= "yyyy-MM-dd HH";
    public static final DateTimeFormatter DATE_TIME_ID_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_ID_PATTERN).withZone(ZONE);
    public static final DateTimeFormatter READ_FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(READ_FILE_DATE_TIME_PATTERN).withZone(ZONE);

}
