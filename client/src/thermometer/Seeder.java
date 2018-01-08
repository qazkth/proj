/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermometer;

import controller.Controller;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Oscar
 */
// Seeds stream and database with random data from <daysToSeed> days ago
public class Seeder {
    private final int numOfIds;
    private final long startDay;
    private final Random rand;
    private final long AT_LEAST_X_CHANGES_PER_DAY = 3;

    public Seeder(int numOfIds, int daysToSeed) {
        this.numOfIds = numOfIds;
        this.startDay = getStartDay(daysToSeed);
        this.rand = new Random();
    }
    
    public void seed() {
        long countOuter = 0;
        for(int i = 1; i <= numOfIds; i++) {
            long countInner = 0;
            System.out.println("Start seeding thermo: " + i);
            long timestampPast = startDay;
            long timestampToday = System.currentTimeMillis();
            Controller contr = new Controller(i);
            int currTemperature = rand.nextInt(20) + 10;
            
            while((timestampPast = nextTimestamp(timestampPast)) < timestampToday) {
                contr.seed(nextTemperature(currTemperature), timestampPast);
                countInner++;
            }
            
            System.out.println("inner: " + countInner);
            countOuter += countInner;
        }
        System.out.println("outer: " + countOuter);
    }

    private long getStartDay(int daysAgo) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        ZonedDateTime startOfToday = zdt.toLocalDate().atStartOfDay(ZoneId.of("UTC"));
        
        return startOfToday.minusDays(daysAgo).toEpochSecond() * 1000;
    }
    
    private int nextTemperature(int temperature) {
        return rand.nextInt() % 2 == 0 ? temperature + 1 : temperature - 1;
    }
    
    private long nextTimestamp(long timestamp) {
        int notMoreThanMillies = (int) TimeUnit.HOURS.toMillis(24 / AT_LEAST_X_CHANGES_PER_DAY);
        return timestamp + rand.nextInt(notMoreThanMillies - 1) + 1;
    }
}
