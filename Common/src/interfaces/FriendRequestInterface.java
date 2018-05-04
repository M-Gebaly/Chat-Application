/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.User;

/**
 *
 * @author Asmaa
 */
public interface FriendRequestInterface extends Remote {
    public boolean sendRequest(long id,String friendEmail) throws RemoteException;
    public ArrayList<User> getRequestsList(long id) throws RemoteException;
    public void acceptRequest(long userID,long friendID)throws RemoteException;
    public void rejectRequest(long userID,long friendID)throws RemoteException;
    
}
