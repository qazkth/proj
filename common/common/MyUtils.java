/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author Oscar
 */
public class MyUtils {

    public long getKeyDateDaysFromNow(int days) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        ZonedDateTime startOfToday = zdt.toLocalDate().atStartOfDay(ZoneId.of("UTC"));

        return startOfToday.minusDays(days).toEpochSecond() * 1000;
    }

    public boolean isPastDaysTimeBeforeTimestamp(int days, long timestamp) {
        long timeInPast = getKeyDateDaysFromNow(days);
        return timeInPast <= timestamp;
    }
    
    public DataCounter myDataCounterAggregator(TemperaturePojo newValue, DataCounter aggValue) {
        ++aggValue.count;

        aggValue.currentTemp = newValue.temperature;
        
        double oldMean = aggValue.mean;
        aggValue.mean += (newValue.temperature - aggValue.mean) / aggValue.count;

        aggValue.variance
                = ((aggValue.count - 1) * aggValue.variance
                + (newValue.temperature - aggValue.mean) * (newValue.temperature - oldMean)) / aggValue.count;

        aggValue.standardDeviation = Math.sqrt(aggValue.variance);

        if (newValue.temperature <= aggValue.minTemp) {
            aggValue.minTemp = newValue.temperature;
            aggValue.minTempThermometerId = newValue.thermometer_id;
            aggValue.minTempReadTime = newValue.read_time_millies;
        }

        if (newValue.temperature >= aggValue.maxTemp) {
            aggValue.maxTemp = newValue.temperature;
            aggValue.maxTempThermometerId = newValue.thermometer_id;
            aggValue.maxTempReadTime = newValue.read_time_millies;
        }

        aggValue.firstReadTime = aggValue.firstReadTime >= newValue.read_time_millies
                ? newValue.read_time_millies : aggValue.firstReadTime;

        aggValue.lastReadTime = aggValue.lastReadTime <= newValue.read_time_millies
                ? newValue.read_time_millies : aggValue.lastReadTime;

        return aggValue;
    }
}