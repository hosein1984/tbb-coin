package org.tbb.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    public static Instant parse(String date) throws DateTimeParseException {
        return Instant.parse(date);
    }

    public static Instant mustParse(String date) {
        try {
            return parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

    public static long unix() {
        return Instant.now().getEpochSecond();
    }

    public static Instant fromUnix(long unix) {
        return Instant.ofEpochSecond(unix);
    }

    public static String format(Instant instant) {
        return FORMATTER.format(instant);
    }
}
