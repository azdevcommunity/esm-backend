package com.example.medrese.Core.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateUtil {


    public static LocalDateTime getCurrentDateTime( ) {
        return ZonedDateTime.now(ZoneId.of("Asia/Baku")).toLocalDateTime();
    }
}
