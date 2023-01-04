package com.son.daou.util;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final ZoneId zone = ZoneId.of("Asia/Seoul");
    public static final DateTimeFormatter DATE_TIME_ID_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH").withZone(zone);
    public static final DateTimeFormatter READ_FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH").withZone(zone);

}
