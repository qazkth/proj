/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermometer;

import controller.Controller;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Oscar
 */
// Represents a device
public class Thermometer implements Runnable {
    private final int id;
    private final Controller contr;
    private final Sensor sensor;
    private final int temperatureReadIntervalSecs;
    private final long mainThreadSleepTimeMillies;

    public Thermometer(int id) {
        this.id = id;
        this.contr = new Controller(this.id);
        this.sensor = new Sensor();
        this.temperatureReadIntervalSecs = 1;
        this.mainThreadSleepTimeMillies = TimeUnit.SECONDS.toMillis(30);
    }
    
    @Override
    public void run() {
        System.out.println("Thermometer: " + id + " is alive");
        
        // Timer for the sensor reading the temperature, 
        // runs first time after 3 secs then every <this.temperatureReadInterval> secs
        final ScheduledExecutorService sensorScheduler = Executors.newSingleThreadScheduledExecutor();
        sensorScheduler.scheduleAtFixedRate(this.sensor, 3, this.temperatureReadIntervalSecs, TimeUnit.SECONDS);
                
        // do thermometer related work, working on other stuff such a device handles
        // maybe stuff like wind speed/direction or rain
        // the only thing this thread does is sleeps, lazy....
        while(true) {
            try {
                Thread.sleep(this.mainThreadSleepTimeMillies);
            } catch (InterruptedException ex) {}
        }
    }
    
    // Represents a sensor in a device
    private class Sensor implements Runnable {
        private volatile double temp;
        private final Random random;

        private Sensor() {
            this.random = new Random(System.currentTimeMillis());
            this.temp = this.random.nextInt(50) - 15; // start temperature between [-15, 45)
        }
        
        @Override
        public void run() {
            int upOrDown = this.random.nextInt(); // Used to descide increase/decrease of temp
            this.temp += (upOrDown % 2 == 0) ? this.random.nextDouble() : (-1.0) * this.random.nextDouble();
            contr.newTemperatureReading(this.temp, System.currentTimeMillis());
        }
        
    }
    
}
