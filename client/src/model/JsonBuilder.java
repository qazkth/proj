/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Oscar
 */
public class JsonBuilder {
    
    public String temperatureToJsonString(int id, int temp, long time) {
        return "{"
                + "\"thermometer_id\":\"" + id +"\""
                + ", \"temperature\":\"" + temp + "\""
                + ", \"read_time_millies\":\"" + time + "\""
                + "}";
    }
    
}
