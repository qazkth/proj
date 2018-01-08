/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Oscar
 */
public class TemperatureDbHandler {
    private final String dbURL = "jdbc:derby://localhost:1527/Thermometers;create=true;user=ID1212;password=ID1212";
    private final String tempTable = "temperature";
    private final String thermTable = "thermometer";

    private Connection conn;
    private PreparedStatement insertTemp;
    private PreparedStatement listLocationThermos;


    public TemperatureDbHandler() {
        initConnectionAndStatements();
    }

    private void initConnectionAndStatements() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            this.conn = DriverManager.getConnection(dbURL);
            this.conn.setAutoCommit(false);
            initStatements();
        } catch (Exception except) {
            System.out.println("connection error: " + except.getMessage());
            except.printStackTrace();
        }
    }
    
    public ArrayList<Integer> getLandThermometers() {
        return getThermometersInLocation("land");
    }
    
    public ArrayList<Integer> getOceanThermometers() {
        return getThermometersInLocation("ocean");
    }
    
    private ArrayList<Integer> getThermometersInLocation(String location) {
        ArrayList<Integer> thermometerIds = new ArrayList<>();
        
        try {
            this.listLocationThermos.setString(1, location);
            ResultSet rs = this.listLocationThermos.executeQuery();
            
            while(rs.next()) {
                thermometerIds.add(rs.getInt("id"));
            }
            
        } catch (Exception ex) {
            System.out.println("query error: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return thermometerIds;
    }
    
    public synchronized void addTemp(int id, int temp, long timeInMillies) {
        try {
            this.insertTemp.setInt(1, id);
            this.insertTemp.setInt(2, temp);
            this.insertTemp.setTimestamp(3, new Timestamp(timeInMillies));
            this.insertTemp.executeUpdate();
            this.conn.commit();
        } catch(Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void initStatements() throws SQLException {
        this.insertTemp = this.conn.prepareStatement("INSERT INTO " + this.tempTable
                + " (thermometer_id, temperature, read_time) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS
        );
        
        this.listLocationThermos = this.conn.prepareStatement("SELECT id as id"
                + " FROM " + this.thermTable
                + " WHERE location = ?"
        );
        
    }

}
