/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startup;

import controller.Controller;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Oscar
 */
public class Server {
    public static void main(String[] args) throws Exception {        
        try {
            new Server().startRegistry();
            Controller contr = new Controller();
            contr.startStreams();
            Naming.rebind(Controller.SERVER_NAME_IN_REGISTRY, contr);
            System.out.println("Server is running");
        } catch (Exception ex) {
            System.err.println("Server exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
    
    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (Exception ex) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }

}
