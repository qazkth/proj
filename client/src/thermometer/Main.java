/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermometer;

/**
 *
 * @author Oscar
 */
public class Main {
    // Option for running this app
    private static final String SIMULATE = "--simulate";
    private static final String SEED = "--seed";
    // Hardcoded #, same as # thermometer ids used to simulate thermometers
    private static final int NUM_OF_IDS = 13;
    // Seed days
    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 1825;
    
    public static void main(String[] args) {
        
        if(args.length < 1) {
            printUsageAndQuit();
        }
        
        switch(args[0]) {
            case SIMULATE:
                for (int i = 1; i <= NUM_OF_IDS; i++) {
                    new Thread(new Thermometer(i)).start();
                }
                break;
            case SEED:
                int daysToSeed = 0;
                try {
                    daysToSeed = Integer.parseInt(args[1]);
                    if(daysToSeed < MIN_DAYS || daysToSeed > MAX_DAYS) { throw new Exception(); }
                } catch(Exception ex) { printUsageAndQuit(); }
                new Seeder(NUM_OF_IDS, daysToSeed).seed();
                break;
            default:
                printUsageAndQuit();
        }
        
    }
    
    private static void printUsageAndQuit() {
        System.out.println(
                "\nusage alt.1:\t"+ SIMULATE + "\n"
                + "usage alt.2:\t" + SEED + " <Integer: " + MIN_DAYS + " ≤ days to seed ≤ " + MAX_DAYS + ">\n"
        );
        System.exit(1);
    }
    
}
