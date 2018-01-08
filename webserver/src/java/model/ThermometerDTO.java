/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author Oscar
 */
public interface ThermometerDTO {
    
    public Integer getId();
    
    public OwnerDTO getOwnerId();
    
    public BigDecimal getLatitude();
    
    public BigDecimal getLongitude();
    
    public String getLocation();
    
    public String getLocationName();
    
    /*
    owner_id INTEGER not null,
    latitude DECIMAL(8,6) not null,
    longitude DECIMAL(9,6) not null,
    location VARCHAR(255) not null,
    location_name VARCHAR (255) not null,
    */
    
}
