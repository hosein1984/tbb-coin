package org.tbb.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public class DateUtils {
    public static Date parse(String date) throws ParseException {
        return Date.from(Instant.parse(date));
    }

    public static Date mustParse(String date) {
        try {
            // "2019-03-18T00:00:00.000000000Z" fails?
            return parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }

    public static long unix() {
        return Instant.now().getEpochSecond();
    }

    public static Date fromUnix(long unix) {
        return Date.from(Instant.ofEpochSecond(unix));
    }

    public static Object format(Date genesisTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
        return dateFormat.format(genesisTime);
    }
}
