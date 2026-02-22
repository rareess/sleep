package com.noom.interview.fullstack.sleep.util;

import java.time.Duration;
import java.time.LocalTime;

public final class SleepUtil {

    private SleepUtil() {}

    /**
     * calculate minutes in bed based on bed time and wake time
     * special case when bed time is <24 but wake time is >24
     * ex. bed time 22:00, wake time 6:00 -> there should be 480 minutes but Duration.between(22:00,6:00) will be -960 minutes
     * adding 24h => 1440minutes we got the right value in this case
     * bed time 22, wake time 6:00 -> will be 480 ( -960 + 1440)
     */
    public static int calculateMinutesInBed(LocalTime bedTime, LocalTime wakeTime) {
        long minutes = Duration.between(bedTime, wakeTime).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        return (int) minutes;
    }
}
