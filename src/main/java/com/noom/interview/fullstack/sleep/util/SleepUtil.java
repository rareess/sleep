package com.noom.interview.fullstack.sleep.util;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public final class SleepUtil {

    private SleepUtil() {}

    /**
     * calculate minutes in bed based on bedtime and wake time
     * special case when bedtime is <24 but wake time is >24
     * ex. bedtime 22:00, wake time 6:00 -> there should be 480 minutes but Duration.between(22:00,6:00) will be -960 minutes
     * adding 24h => 1440minutes we got the right value in this case
     * bedtime 22, wake time 6:00 -> will be 480 ( -960 + 1440)
     */
    public static int calculateMinutesInBed(LocalTime bedTime, LocalTime wakeTime) {
        long minutes = Duration.between(bedTime, wakeTime).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        return (int) minutes;
    }

    public static LocalTime averageBedTime(List<LocalTime> bedTimes) {
        if (bedTimes.isEmpty()) return null;
        long totalMinutes = bedTimes.stream()
                .mapToLong(t -> {
                    long minutes = t.getHour() * 60L + t.getMinute();
                    if (t.getHour() < 12) minutes += 24 * 60;
                    return minutes;
                })
                .sum();
        long avgMinutes = (totalMinutes / bedTimes.size()) % (24 * 60);
        return LocalTime.of((int) (avgMinutes / 60), (int) (avgMinutes % 60));
    }

    public static LocalTime averageWakeTime(List<LocalTime> wakeTimes) {
        if (wakeTimes.isEmpty()) return null;
        long totalMinutes = wakeTimes.stream()
                .mapToLong(t -> t.getHour() * 60L + t.getMinute())
                .sum();
        long avgMinutes = totalMinutes / wakeTimes.size();
        return LocalTime.of((int) (avgMinutes / 60), (int) (avgMinutes % 60));
    }
}
