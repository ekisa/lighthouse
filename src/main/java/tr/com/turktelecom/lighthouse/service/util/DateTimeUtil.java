package tr.com.turktelecom.lighthouse.service.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by 010235 on 03.05.2016.
 */
public final class DateTimeUtil {
    public enum PATTERN {
        DATE_TIME_PATTERN_FOR_FILE_NAMES("yyyy-MM-dd-HH-mm-ss"),
        DATE_TIME_PATTERN("yyyy-MM-dd HH:mm:ss");

        private String pattern;

        PATTERN(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String toString() {
            return this.pattern;
        }
    }

    public static String formatTimeStamp(ZonedDateTime time, PATTERN pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern.toString()));
    }

}
