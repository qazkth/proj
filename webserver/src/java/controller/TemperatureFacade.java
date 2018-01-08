/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import common.MyConstants;
import common.MyUtils;
import integration.ThermometerDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import model.OwnerDTO;
import common.StreamServer;
import common.TemperatureStatiticsDTO;
import model.ThermometerDTO;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.PostConstruct;

/**
 *
 * @author Oscar
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class TemperatureFacade {

    @EJB
    ThermometerDAO thermometerDAO;
    private final MyUtils utils = new MyUtils();
    private StreamServer streamServer;
    private int streamServerConnectTries = 0;
    private final int maxStreamServerConnectTries = 5;
    
    @PostConstruct
    private void connectStreamsServer() {
        try {
            streamServerConnectTries++;
            streamServer = (StreamServer) Naming.lookup(StreamServer.SERVER_NAME_IN_REGISTRY);
            resetStreamServerConnectTries();
        } catch (Exception ex) {
            // Failed connecting to the stream server, database will be used instead
        }
    }

    public List<? extends OwnerDTO> listOwners() {
        return thermometerDAO.listOwners();
    }

    public List<? extends ThermometerDTO> listThermometers() {
        return thermometerDAO.listThermometers();
    }

    public List<String> getSelectableThermometerIds() {
        ArrayList<String> thermoIds = new ArrayList<>(Arrays.asList(MyConstants.STRING_THERMOMETERS));

        thermometerDAO.listThermometers().forEach((thermometer) -> {
            try {
                thermoIds.add(thermometer.getId().toString());
            } catch (Exception ex) {}
        });

        return thermoIds;
    }

    public List<Integer> getPredefinedIntegerPastDays() {
        return new ArrayList<>(Arrays.asList(MyConstants.PREDEFINED_INT_PERIODS));
    }

    public List<String> getPredefinedStringPeriods() {
        return new ArrayList<>(Arrays.asList(MyConstants.PREDEFINED_STRING_PERIODS));
    }

    public List<? extends TemperatureStatiticsDTO> getDataFromPredefinedDays(String selectedThermometer, String selectedDays) {
        List<? extends TemperatureStatiticsDTO> temperatureStats = new ArrayList<>();

        boolean allTimeDataFetch = selectedDays.equals(MyConstants.ALL_TIME);
        boolean fetchFromStreamServer = allTimeDataFetch && streamServerConnectionTriesLeft();
        
        try {
            if(fetchFromStreamServer) {
                try {
                    temperatureStats = streamServer.getAlltimeData(selectedThermometer);
                } catch (RemoteException|NullPointerException streamServerConnectionProblems) {
                    connectStreamsServer();
                    return getDataFromPredefinedDays(selectedThermometer, selectedDays);
                } catch (Exception unknowStreamServerProblems) {
                    setStreamServerConnectTriesToMax();
                    return getDataFromPredefinedDays(selectedThermometer, selectedDays);
                }
            } else {
                int pastDays = allTimeDataFetch ? Integer.MAX_VALUE : Integer.parseInt(selectedDays);
                temperatureStats = getDataFromPastDays(selectedThermometer, pastDays);
            }
        } catch(Exception ex) {
            // Issues with both stream server and the database
        }
        
        resetStreamServerConnectTries();
        return temperatureStats;
    }

    public List<? extends TemperatureStatiticsDTO> getDataFromCustomDays(String customDayThermometer, Integer customSelectedDays) {
        if(thermometerDAO.isPastDaysBeforeFirstReading(customSelectedDays)) {
            return getDataFromPredefinedDays(customDayThermometer, MyConstants.ALL_TIME);
        } else {
            return getDataFromPastDays(customDayThermometer, customSelectedDays);
        }
    }
    
    private List<? extends TemperatureStatiticsDTO> getDataFromPastDays(String thermometerId, Integer numberOfPastDays) {
        List<? extends TemperatureStatiticsDTO> temperatureStats = new ArrayList<>();

        long timeAtStartOfPastDays = utils.getKeyDateDaysFromNow(numberOfPastDays);
        
        boolean insideStreamWindowTime = numberOfPastDays <= MyConstants.MAX_STREAM_YEAR_WINDOW_SIZE;
        boolean fetchFromStreamServer =  insideStreamWindowTime && streamServerConnectionTriesLeft();
        
        try {
            if(fetchFromStreamServer) {
                try {
                    temperatureStats = streamServer.getWindowedData(thermometerId, numberOfPastDays);
                } catch (RemoteException|NullPointerException streamServerConnectionProblems) {
                    connectStreamsServer();
                    return getDataFromPastDays(thermometerId, numberOfPastDays);
                } catch (Exception unknowStreamServerProblems) {
                    setStreamServerConnectTriesToMax();
                    return getDataFromPastDays(thermometerId, numberOfPastDays);
                }
            } else {
                switch(thermometerId) {
                    case MyConstants.ALL_THERMOMETERS:
                        temperatureStats = thermometerDAO.getAllPastDaysData(timeAtStartOfPastDays);
                        break;
                    case MyConstants.ALL_LAND_THERMOMETERS:
                        temperatureStats = thermometerDAO.getLocationPastDaysData(MyConstants.LOCATION_LAND, timeAtStartOfPastDays);
                        break;
                    case MyConstants.ALL_OCEAN_THERMOMETERS:
                        temperatureStats = thermometerDAO.getLocationPastDaysData(MyConstants.LOCATION_OCEAN, timeAtStartOfPastDays);
                        break;
                    default:
                        int thermId = Integer.parseInt(thermometerId);
                        temperatureStats = thermometerDAO.getSpecificPastDaysData(thermId, timeAtStartOfPastDays);
                        break;
                }
            }
        } catch (Exception ex) {
            // Issues with both stream server and the database
        }
        
        resetStreamServerConnectTries();
        return temperatureStats;
    }

    public List<? extends TemperatureStatiticsDTO> getDataFromCustomPeriod(String customPeriodThermometer, long startTime, long endTime) {
        List<? extends TemperatureStatiticsDTO> temperatureStats = new ArrayList<>();

        try {
            int thermometerId = Integer.parseInt(customPeriodThermometer);
            temperatureStats = thermometerDAO.getThermometerDataBetweenPeriod(thermometerId, startTime, endTime);
        } catch (NumberFormatException nex) {
            switch (customPeriodThermometer) {
                case MyConstants.ALL_THERMOMETERS:
                    temperatureStats = thermometerDAO.getAllDataBetweenPeriod(startTime, endTime);
                    break;
                case MyConstants.ALL_LAND_THERMOMETERS:
                    temperatureStats = thermometerDAO.getLocationDataBetweenPeriod(MyConstants.LOCATION_LAND, startTime, endTime);
                    break;
                case MyConstants.ALL_OCEAN_THERMOMETERS:
                    temperatureStats = thermometerDAO.getLocationDataBetweenPeriod(MyConstants.LOCATION_OCEAN, startTime, endTime);
                    break;
            }
        }

        return temperatureStats;
    }
    
    private boolean streamServerConnectionTriesLeft() {
        return streamServerConnectTries < maxStreamServerConnectTries;
    }
    
    private void resetStreamServerConnectTries() { 
        streamServerConnectTries = 0; 
    }
    
    private void setStreamServerConnectTriesToMax() {
        streamServerConnectTries = maxStreamServerConnectTries;
    }
    
}
