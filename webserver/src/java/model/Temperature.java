/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oscar
 */
@Entity
@Table(name = "TEMPERATURE")
@NamedQueries({
    @NamedQuery(name = "Temperature.findAll", query = "SELECT t FROM Temperature t")
    , @NamedQuery(name = "Temperature.findFirstTime", query = "SELECT t FROM Temperature t ORDER BY t.temperaturePK.readTime asc")
    , @NamedQuery(name = "Temperature.findByThermometerId", query = "SELECT t FROM Temperature t WHERE t.temperaturePK.thermometerId = :thermometerId")
    , @NamedQuery(name = "Temperature.findByTemperature", query = "SELECT t FROM Temperature t WHERE t.temperaturePK.temperature = :temperature")
    , @NamedQuery(name = "Temperature.findByReadTime", query = "SELECT t FROM Temperature t WHERE t.temperaturePK.readTime = :readTime")
    , @NamedQuery(name = "Temperature.findAllPastDays", query = "SELECT t FROM Temperature t WHERE t.temperaturePK.readTime >= :readTime")
    , @NamedQuery(name = "Temperature.findLocationPastDays", query = "SELECT t FROM Temperature t WHERE (t.temperaturePK.readTime >= :readTime) AND (t.thermometer.location = :location)")
    , @NamedQuery(name = "Temperature.findSpecificPastDays", query = "SELECT t FROM Temperature t WHERE (t.temperaturePK.readTime >= :readTime) AND (t.temperaturePK.thermometerId = :tId)")
    , @NamedQuery(name = "Temperature.findAllBetweenDates", query = "SELECT t FROM Temperature t WHERE t.temperaturePK.readTime BETWEEN :startDate AND :endDate")
    , @NamedQuery(name = "Temperature.findSpecificBetweenDates", query = "SELECT t FROM Temperature t WHERE (t.temperaturePK.readTime BETWEEN :startDate AND :endDate) AND (t.temperaturePK.thermometerId = :tId)")
    , @NamedQuery(name = "Temperature.findAllLocationBetweenDates", query = "SELECT t FROM Temperature t WHERE (t.temperaturePK.readTime BETWEEN :startDate AND :endDate) AND (t.thermometer.location = :location)")})
public class Temperature implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TemperaturePK temperaturePK;
    @JoinColumn(name = "THERMOMETER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Thermometer thermometer;

    public Temperature() {
    }

    public Temperature(TemperaturePK temperaturePK) {
        this.temperaturePK = temperaturePK;
    }

    public Temperature(int thermometerId, int temperature, Date readTime) {
        this.temperaturePK = new TemperaturePK(thermometerId, temperature, readTime);
    }

    public TemperaturePK getTemperaturePK() {
        return temperaturePK;
    }

    public void setTemperaturePK(TemperaturePK temperaturePK) {
        this.temperaturePK = temperaturePK;
    }

    public Thermometer getThermometer() {
        return thermometer;
    }

    public void setThermometer(Thermometer thermometer) {
        this.thermometer = thermometer;
    }

}
