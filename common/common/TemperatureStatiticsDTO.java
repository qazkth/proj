/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Oscar
 */
public interface TemperatureStatiticsDTO {
    
    public String getSource();
    
    public long getCount();
    
    public int getCurrentTemp();
    
    public Double getMean();
    
    public Double getVariance();
    
    public Double getStandardDeviation();
    
    public Integer getMaxTemp();
    
    public Integer getMaxTempThermometerId();
    
    public Long getMaxTempReadTime();
    
    public Integer getMinTemp();
    
    public Integer getMinTempThermometerId();
    
    public Long getMinTempReadTime();
    
    public Long getFirstReadTime();
    
    public Long getLastReadTime();

}
