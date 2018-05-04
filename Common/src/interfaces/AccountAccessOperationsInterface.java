/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.User;

/**
 *
 * @author Asmaa
 */
public interface AccountAccessOperationsInterface extends Remote{
    public User login(String email,String password) throws RemoteException;
    public boolean signUp(User user) throws RemoteException;
    
}
