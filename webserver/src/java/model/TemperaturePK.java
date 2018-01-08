/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Oscar
 */
@Embeddable
public class TemperaturePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "THERMOMETER_ID")
    private int thermometerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMPERATURE")
    private int temperature;
    @Basic(optional = false)
    @NotNull
    @Column(name = "READ_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime;

    public TemperaturePK() {
    }

    public TemperaturePK(int thermometerId, int temperature, Date readTime) {
        this.thermometerId = thermometerId;
        this.temperature = temperature;
        this.readTime = readTime;
    }

    public int getThermometerId() {
        return thermometerId;
    }

    public void setThermometerId(int thermometerId) {
        this.thermometerId = thermometerId;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

}
