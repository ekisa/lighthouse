package tr.com.turktelecom.lighthouse.service.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by 010235 on 03.05.2016.
 */
public final class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd-HH-mm-ss";

    public static String formatTimeStamp(ZonedDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return time.format(dateTimeFormatter);
    }
}
