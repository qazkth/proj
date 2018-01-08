/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TemperatureFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import model.OwnerDTO;
import model.ThermometerDTO;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Oscar
 */
@Named("presentationManager")
@SessionScoped
public class PresentationManager implements Serializable {
    @EJB
    private TemperatureFacade temperatureFacade;
    private List<? extends OwnerDTO> owners;
    private List<? extends ThermometerDTO> thermometers;
    
    @PostConstruct
    private void initLists() {
        this.owners = temperatureFacade.listOwners();
        this.thermometers = temperatureFacade.listThermometers();
    }
    
    /* GETTERS AND SETTERS */
    
    public List<? extends OwnerDTO> getOwners() {
        return owners;
    }

    public void setOwners(List<? extends OwnerDTO> owners) {
        this.owners = owners;
    }

    public List<? extends ThermometerDTO> getThermometers() {
        return thermometers;
    }

    public void setThermometers(List<? extends ThermometerDTO> thermometers) {
        this.thermometers = thermometers;
    }
    
    
    
}
