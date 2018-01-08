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
public class TemperatureReadings {
    private volatile int currTemp = Integer.MIN_VALUE;
    
    public boolean isValidChange(double newTemp) {
        int newTempRounded = (int) newTemp;
        
        if(this.currTemp != newTempRounded) {
            this.currTemp = newTempRounded;
            return true;
        } else {
            return false;
        }
    }
}
