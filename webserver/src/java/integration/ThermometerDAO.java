/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import common.MyUtils;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Owner;
import model.OwnerDTO;
import common.TemperatureStatiticsDTO;
import model.Thermometer;
import model.ThermometerDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import common.DataCounter;
import common.MyConstants;
import model.Temperature;
import common.TemperaturePojo;

/**
 *
 * @author Oscar
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class ThermometerDAO {

    @PersistenceContext(unitName = "thermometerPU")
    private EntityManager em;
    private volatile long firstTemperatureReading;
    private final MyUtils utils = new MyUtils();
    
    @PostConstruct
    private void firstTemperatureOcurrence() {
        Temperature temp = em.createNamedQuery("Temperature.findFirstTime", Temperature.class)
                .setMaxResults(1)
                .getSingleResult();
        
        firstTemperatureReading = temp.getTemperaturePK().getReadTime().getTime();
    }
    
    public List<? extends OwnerDTO> listOwners() {
        return em.createNamedQuery("Owner.findAll", Owner.class).getResultList();
    }

    public List<? extends ThermometerDTO> listThermometers() {
        return em.createNamedQuery("Thermometer.findAll", Thermometer.class).getResultList();
    }

    public List<? extends TemperatureStatiticsDTO> getAllDataBetweenPeriod(long startTime, long endTime) {
        List<Temperature> temps = em.createNamedQuery("Temperature.findAllBetweenDates", Temperature.class)
                .setParameter("startDate", new Timestamp(startTime))
                .setParameter("endDate", new Timestamp(endTime))
                .getResultList();

        return countStats(temps);
    }

    public List<? extends TemperatureStatiticsDTO> getLocationDataBetweenPeriod(String location, long startTime, long endTime) {
        List<Temperature> temps = em.createNamedQuery("Temperature.findAllLocationBetweenDates", Temperature.class)
                .setParameter("location", location)
                .setParameter("startDate", new Timestamp(startTime))
                .setParameter("endDate", new Timestamp(endTime))
                .getResultList();

        return countStats(temps);
    }

    public List<? extends TemperatureStatiticsDTO> getThermometerDataBetweenPeriod(int tID, long startTime, long endTime) {
        List<Temperature> temps = em.createNamedQuery("Temperature.findSpecificBetweenDates", Temperature.class)
                .setParameter("tId", tID)
                .setParameter("startDate", new Timestamp(startTime))
                .setParameter("endDate", new Timestamp(endTime))
                .getResultList();
        
        return countStats(temps);
    }
    
    public List<? extends TemperatureStatiticsDTO> getAllPastDaysData(long dataAfterThisTime) {
        dataAfterThisTime = sanityCheckDesiredStart(dataAfterThisTime);
        List<Temperature> temps = em.createNamedQuery("Temperature.findAllPastDays", Temperature.class)
                .setParameter("readTime", new Timestamp(dataAfterThisTime))
                .getResultList();
        
        return countStats(temps);
    }
    
    public List<? extends TemperatureStatiticsDTO> getLocationPastDaysData(String location, long dataAfterThisTime) {
        dataAfterThisTime = sanityCheckDesiredStart(dataAfterThisTime);
        List<Temperature> temps = em.createNamedQuery("Temperature.findLocationPastDays", Temperature.class)
                .setParameter("location", location)
                .setParameter("readTime", new Timestamp(dataAfterThisTime))
                .getResultList();
        
        return countStats(temps);
    }
    
    public List<? extends TemperatureStatiticsDTO> getSpecificPastDaysData(int thermometerId, long dataAfterThisTime) {
        dataAfterThisTime = sanityCheckDesiredStart(dataAfterThisTime);
        List<Temperature> temps = em.createNamedQuery("Temperature.findSpecificPastDays", Temperature.class)
                .setParameter("tId", thermometerId)
                .setParameter("readTime", new Timestamp(dataAfterThisTime))
                .getResultList();
        
        return countStats(temps);
    }
    
    private long sanityCheckDesiredStart(long startTime) {
        return startTime < firstTemperatureReading ? firstTemperatureReading : startTime;
    }
    
    private List<DataCounter> countStats(List<Temperature> temps) {
        DataCounter aggregate = new DataCounter();
        aggregate.source = MyConstants.SOURCE_DATABASE;

        temps.forEach(temperature -> {
            TemperaturePojo tempPojo = new TemperaturePojo();
            tempPojo.thermometer_id = temperature.getTemperaturePK().getThermometerId();
            tempPojo.temperature = temperature.getTemperaturePK().getTemperature();
            tempPojo.read_time_millies = temperature.getTemperaturePK().getReadTime().getTime();
            utils.myDataCounterAggregator(tempPojo, aggregate);
        });

        // prints the statistics to standard output
        //System.out.println("aggregate: " + aggregate.toString());
        
        List<DataCounter> aggregates = new ArrayList<>();
        aggregates.add(aggregate);
        
        return aggregates;
    }

    public boolean isPastDaysBeforeFirstReading(int days) {
        return utils.isPastDaysTimeBeforeTimestamp(days, firstTemperatureReading);
    }

}
