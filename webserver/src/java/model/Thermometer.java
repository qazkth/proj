/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Oscar
 */
@Entity
@Table(name = "THERMOMETER")
@NamedQueries({
    @NamedQuery(name = "Thermometer.findAll", query = "SELECT t FROM Thermometer t")
    , @NamedQuery(name = "Thermometer.findById", query = "SELECT t FROM Thermometer t WHERE t.id = :id")
    , @NamedQuery(name = "Thermometer.findByLatitude", query = "SELECT t FROM Thermometer t WHERE t.latitude = :latitude")
    , @NamedQuery(name = "Thermometer.findByLongitude", query = "SELECT t FROM Thermometer t WHERE t.longitude = :longitude")
    , @NamedQuery(name = "Thermometer.findByLocation", query = "SELECT t FROM Thermometer t WHERE t.location = :location")
    , @NamedQuery(name = "Thermometer.findByLocationName", query = "SELECT t FROM Thermometer t WHERE t.locationName = :locationName")})
public class Thermometer implements Serializable, ThermometerDTO {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private BigDecimal latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private BigDecimal longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LOCATION")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Owner ownerId;

    public Thermometer() {
    }

    public Thermometer(Integer id) {
        this.id = id;
    }

    public Thermometer(Integer id, BigDecimal latitude, BigDecimal longitude, String location, String locationName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.locationName = locationName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Override
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public Owner getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Owner ownerId) {
        this.ownerId = ownerId;
    }
    
}
