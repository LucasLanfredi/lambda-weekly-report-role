package util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class DateUtils {

    private DateUtils() {}

    public static LocalDate toLocalDate(String isoTimestamp) {
        return Instant.parse(isoTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}