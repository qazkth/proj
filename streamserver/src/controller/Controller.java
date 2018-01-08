/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import common.StreamServer;
import common.TemperatureStatiticsDTO;
import integration.TemperatureDbHandler;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import streams.TemperatureStream;

/**
 *
 * @author Oscar
 */
public class Controller extends UnicastRemoteObject implements StreamServer {
    private final TemperatureDbHandler dbHandler;
    private final TemperatureStream tempStream;

    public Controller() throws RemoteException {
        this.dbHandler = new TemperatureDbHandler();
        this.tempStream = new TemperatureStream(this.dbHandler);
    }
    
    public void startStreams() {
        new Thread(this.tempStream).start();
    }

    @Override
    public synchronized List<? extends TemperatureStatiticsDTO> getWindowedData(String key, Integer days) throws RemoteException, Exception {
        return tempStream.getWindowedData(key, days);
    }

    @Override
    public synchronized List<? extends TemperatureStatiticsDTO> getAlltimeData(String key) throws RemoteException, Exception {
        return tempStream.getAlltimeData(key);
    }
    
}
