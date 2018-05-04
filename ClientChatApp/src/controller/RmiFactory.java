/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import interfaces.ServerInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asmaa
 */
public class RmiFactory {
    private static RmiFactory rmiFactory;
    private Object server;
    private Registry registry;
    private String Ip;
    private boolean check;
    
    private RmiFactory() {
        check = false;
    }
    public Object connect(){
        try {
            if (server==null) {
                registry = LocateRegistry.getRegistry(Ip,2000);
                server =  registry.lookup("chat");
            }
            check = true;
        } catch (RemoteException | NotBoundException ex) {
            check=false;
            Logger.getLogger(RmiFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return server;
    }
    
    public boolean isConnected(){
        return check;
    }
    public static RmiFactory getRmiInstance(){
        if(rmiFactory==null)
            rmiFactory=new RmiFactory();
        return rmiFactory;
            
    }

    /**
     * @return the Ip
     */
    public String getIp() {
        return Ip;
    }

    /**
     * @param Ip the Ip to set
     */
    public void setIp(String Ip) {
        this.Ip = Ip;
    }
    
    
    
    
}
