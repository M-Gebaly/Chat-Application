/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Message;
import model.User;

/**
 *
 * @author Asmaa
 */
public interface ClientInterface extends Remote{
    public void recieve(Message message) throws RemoteException;
    public User getUser()throws RemoteException;
    public void recieveNotification (String message)throws RemoteException;
    public void refreshContacts()throws RemoteException;
    public void reciveFile( String filename, byte[] data, int dataLength) throws RemoteException;
    public void refreshRequests()throws RemoteException;
    public void recieveGroupMessage(Message message , String gid , String gName , ArrayList<User> groupList) throws RemoteException;
}
