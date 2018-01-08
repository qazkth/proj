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
public class MyConstants {
    public static final String SOURCE_STREAM_PERMANENT = "ALLTIME STREAM";
    public static final String SOURCE_STREAM_WINDOW = "WINDOWED STREAM";
    public static final String SOURCE_DATABASE = "DATABASE";
    
    public static final String ALL_THERMOMETERS = "All thermometers";
    public static final String ALL_LAND_THERMOMETERS = "All land thermometers";
    public static final String ALL_OCEAN_THERMOMETERS = "All ocean thermometers";
    
    public static final String LOCATION_LAND = "land";
    public static final String LOCATION_OCEAN = "ocean";
    
    public static final String ALL_TIME_PERIOD_STRING = "All time";
    public static final String X_DAY_PERIOD_STRING = "Past X days";
    public static final String CUSTOM_PERIOD_STRING = "Custom period";
    
    public static final String ALL_TIME = "All time";
    public static final String PAST_DAY = "1";
    public static final String PAST_SEVEN_DAYS = "7";
    public static final String PAST_THIRTY_DAYS = "30";
    public static final String PAST_NINETY_DAYS = "90";
    public static final String PAST_YEAR = "365";
    
    public static final int MAX_STREAM_YEAR_WINDOW_SIZE = 365;
    
    public static final String[] STRING_PERIODS = {
        ALL_TIME_PERIOD_STRING,
        X_DAY_PERIOD_STRING,
        CUSTOM_PERIOD_STRING
    };
    
    public static final Integer[] PREDEFINED_INT_PERIODS = {
        Integer.parseInt(PAST_DAY), 
        Integer.parseInt(PAST_SEVEN_DAYS), 
        Integer.parseInt(PAST_THIRTY_DAYS), 
        Integer.parseInt(PAST_NINETY_DAYS), 
        Integer.parseInt(PAST_YEAR)
    };
    
    public static final String[] PREDEFINED_STRING_PERIODS = {
        ALL_TIME,
        PAST_DAY,
        PAST_SEVEN_DAYS,
        PAST_THIRTY_DAYS,
        PAST_NINETY_DAYS,
        PAST_YEAR
    };
    
    public static final String[] STRING_THERMOMETERS = {
        ALL_THERMOMETERS, 
        ALL_LAND_THERMOMETERS, 
        ALL_OCEAN_THERMOMETERS
    };
    
}