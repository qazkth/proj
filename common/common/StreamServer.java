/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Oscar
 */
public interface StreamServer extends Remote {
    public static final String SERVER_NAME_IN_REGISTRY = "STREAM_SERVER";
    public static final String HOST = "localhost";
    public static final int PORT = 4590;
    
    public List<? extends TemperatureStatiticsDTO> getWindowedData(String key, Integer days) throws RemoteException, Exception;
    
    public List<? extends TemperatureStatiticsDTO> getAlltimeData(String key) throws RemoteException, Exception;
    
}
