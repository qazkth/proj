/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Oscar
 */
public class DataCounter implements Serializable, TemperatureStatiticsDTO {
    public String source;
    public long count;
    public int currentTemp = Integer.MIN_VALUE;
    public int maxTemp = Integer.MIN_VALUE;
    public int minTemp = Integer.MAX_VALUE;
    public double mean = 0;
    public double variance = 0;
    public double standardDeviation = 0;
    public int minTempThermometerId = -1;
    public long minTempReadTime = 0;
    public int maxTempThermometerId = -1;
    public long maxTempReadTime = 0;
    public long firstReadTime = Long.MAX_VALUE;
    public long lastReadTime = 0;

    @Override
    public String toString() {
        return "count: " + count
                + ", current temp: " + currentTemp
                + ", mean: " + mean
                + ", variance: " + variance
                + ", sd: " + standardDeviation
                + ", max: " + maxTemp
                + ", min: " + minTemp
                + ", coldest thermoId: " + minTempThermometerId
                + ", warmest thermoId: " + maxTempThermometerId
                + ", coldest thermo read: " + new Date(minTempReadTime).toString()
                + ", warmest thermo read: " + new Date(maxTempReadTime).toString()
                + ", first read time: " + new Date(firstReadTime).toString()
                + ", last read time: " + new Date(lastReadTime).toString()
                + ", source: " + source;
    }

    @Override
    public String getSource() {
        return source;
    }
    
    @Override
    public int getCurrentTemp() {
        return currentTemp;
    }
    
    @Override
    public long getCount() {
        return count;
    }

    @Override
    public Double getMean() {
        return mean;
    }

    @Override
    public Double getVariance() {
        return variance;
    }

    @Override
    public Double getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public Integer getMaxTemp() {
        return maxTemp;
    }

    @Override
    public Integer getMaxTempThermometerId() {
        return maxTempThermometerId;
    }

    @Override
    public Long getMaxTempReadTime() {
        return maxTempReadTime;
    }

    @Override
    public Integer getMinTemp() {
        return minTemp;
    }

    @Override
    public Integer getMinTempThermometerId() {
       return minTempThermometerId;
    }

    @Override
    public Long getMinTempReadTime() {
        return minTempReadTime;
    }

    @Override
    public Long getFirstReadTime() {
        return firstReadTime;
    }

    @Override
    public Long getLastReadTime() {
        return lastReadTime;
    }
    
}