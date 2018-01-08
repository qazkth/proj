/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.JsonBuilder;
import streams.MyKafkaProducer;
import model.TemperatureReadings;

/**
 *
 * @author Oscar
 */
public class Controller {
    private final int themometerId;
    private final JsonBuilder jsonBuilder;
    private final MyKafkaProducer producer;
    private final TemperatureReadings temperatures;

    public Controller(int id) {
        this.themometerId = id;
        this.jsonBuilder = new JsonBuilder();
        this.producer = new MyKafkaProducer(this.themometerId);
        this.temperatures = new TemperatureReadings();
    }
    
    public void newTemperatureReading(double temp, long time) {        
        if(this.temperatures.isValidChange(temp)) {
            send((int) temp, time);
        }
    }
    
    private void send(int temp, long time) {
        String json = buildJson(this.themometerId, temp, time);
        this.producer.send(time, json);
    }
    
    private String buildJson(int id, int temp, long time) {
        return this.jsonBuilder.temperatureToJsonString(id, temp, time);
    }
    
    public void seed(int temp, long timestamp) {
        send(temp, timestamp);
    }
}
