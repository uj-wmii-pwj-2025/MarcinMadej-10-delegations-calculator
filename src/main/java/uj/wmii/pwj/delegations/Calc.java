package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
        ZonedDateTime startDate = ZonedDateTime.parse(start, formatter);
        ZonedDateTime endDate   = ZonedDateTime.parse(end, formatter);

        if (!startDate.isBefore(endDate)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        Duration duration = Duration.between(startDate, endDate);
        BigDecimal result = getBigDecimal(dailyRate, duration);

        return result.setScale(2, RoundingMode.HALF_UP);

    }

    private static BigDecimal getBigDecimal(BigDecimal dailyRate, Duration duration) {
        long totalMinutes = duration.toMinutes();

        long fullDays = totalMinutes / (24 * 60);
        long remainingMinutes = totalMinutes % (24 * 60);

        BigDecimal result = dailyRate.multiply(BigDecimal.valueOf(fullDays));

        if (remainingMinutes > 0) {
            BigDecimal fraction;

            if (remainingMinutes <= 8 * 60) {
                fraction = BigDecimal.ONE.divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP);
            } else if (remainingMinutes <= 12 * 60) {
                fraction = BigDecimal.ONE.divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);
            } else {
                fraction = BigDecimal.ONE;
            }

            result = result.add(dailyRate.multiply(fraction));
        }
        return result;
    }
}
