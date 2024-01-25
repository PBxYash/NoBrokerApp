package com.nobroker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDuration {
    private long days;
    private long hours;
    private long minutes;
//    public void reduceByOneMinute() {
//        Duration duration = Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
//        duration = duration.minusMinutes(1);
//        days = duration.toDays();
//        hours = duration.toHoursPart();
//        minutes = duration.toMinutesPart();
//    }
    public void reduceByOneMinute() {
        if (minutes > 0) {
            minutes--;
        }
    }

    public void reduceByOneHour() {
        if (hours > 0) {
            hours--;
        }
    }

    public void reduceByOneDay() {
        if (days > 0) {
            days--;
        }
    }
}
