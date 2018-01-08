/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.TemperatureStatiticsDTO;
import controller.TemperatureFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Oscar
 */
@Named("statsManager")
@SessionScoped
public class StatsManager implements Serializable{
    @EJB
    private TemperatureFacade temperatureFacade;
    
    private List<String> selectableThermometerIds;
    private List<String> predefinedStringPeriods;
    
    private List<? extends TemperatureStatiticsDTO> predefinedDaysData;
    private List<? extends TemperatureStatiticsDTO> customDaysData;
    private List<? extends TemperatureStatiticsDTO> customPeriodData;
    
    
    private String selectedThermometer;
    private String selectedDays;
    
    private String customDayThermometer;
    private Integer customSelectedDays;
    
    private String customPeriodThermometer;
    private Date customDateFrom;
    private Date customDateTo;
    
    @PostConstruct
    private void initLists() {
        selectableThermometerIds = temperatureFacade.getSelectableThermometerIds();
        predefinedStringPeriods = temperatureFacade.getPredefinedStringPeriods();
    }

    /* BUTTON ACTION */
    
    public void dataFromPredefinedDays() {
        predefinedDaysData = temperatureFacade.getDataFromPredefinedDays(selectedThermometer, selectedDays);
    }
    
    public void dataFromCustomDays() {
        customDaysData = temperatureFacade.getDataFromCustomDays(customDayThermometer, customSelectedDays);
    }
    
    public void dataFromCustomPeriod() {
        customPeriodData = temperatureFacade.getDataFromCustomPeriod(
                customPeriodThermometer, 
                customDateFrom.getTime(),
                customDateTo.getTime()
        );
    }
    
    /* CONVERTER */
    
    public Date longToDate(long timestamp) {
        return new Date(timestamp);
    }
    
    /* GETTER AND SETTERS */

    public List<String> getSelectableThermometerIds() {
        return selectableThermometerIds;
    }

    public void setSelectableThermometerIds(List<String> selectableThermometerIds) {
        this.selectableThermometerIds = selectableThermometerIds;
    }

    public List<String> getPredefinedStringPeriods() {
        return predefinedStringPeriods;
    }

    public void setPredefinedStringPeriods(List<String> predefinedStringPeriods) {
        this.predefinedStringPeriods = predefinedStringPeriods;
    }

    public List<? extends TemperatureStatiticsDTO> getPredefinedDaysData() {
        return predefinedDaysData;
    }

    public void setPredefinedDaysData(List<? extends TemperatureStatiticsDTO> predefinedDaysData) {
        this.predefinedDaysData = predefinedDaysData;
    }

    public List<? extends TemperatureStatiticsDTO> getCustomDaysData() {
        return customDaysData;
    }

    public void setCustomDaysData(List<? extends TemperatureStatiticsDTO> customDaysData) {
        this.customDaysData = customDaysData;
    }

    public List<? extends TemperatureStatiticsDTO> getCustomPeriodData() {
        return customPeriodData;
    }

    public void setCustomPeriodData(List<? extends TemperatureStatiticsDTO> customPeriodData) {
        this.customPeriodData = customPeriodData;
    }

    public String getSelectedThermometer() {
        return selectedThermometer;
    }

    public void setSelectedThermometer(String selectedThermometer) {
        this.selectedThermometer = selectedThermometer;
    }

    public String getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(String selectedDays) {
        this.selectedDays = selectedDays;
    }

    public String getCustomDayThermometer() {
        return customDayThermometer;
    }

    public void setCustomDayThermometer(String customDayThermometer) {
        this.customDayThermometer = customDayThermometer;
    }

    public Integer getCustomSelectedDays() {
        return customSelectedDays;
    }

    public void setCustomSelectedDays(Integer customSelectedDays) {
        this.customSelectedDays = customSelectedDays;
    }

    public String getCustomPeriodThermometer() {
        return customPeriodThermometer;
    }

    public void setCustomPeriodThermometer(String customPeriodThermometer) {
        this.customPeriodThermometer = customPeriodThermometer;
    }

    public Date getCustomDateFrom() {
        return customDateFrom;
    }

    public void setCustomDateFrom(Date customDateFrom) {
        this.customDateFrom = customDateFrom;
    }

    public Date getCustomDateTo() {
        return customDateTo;
    }

    public void setCustomDateTo(Date customDateTo) {
        this.customDateTo = customDateTo;
    }
    
}
