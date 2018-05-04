/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.FileTransfer;
import model.Message;
import model.User;
import sun.security.jca.GetInstance;


/**
 *
 * @author Asmaa
 */
public interface ServerInterface extends Remote{
    
    public void register(ClientInterface client)throws RemoteException;
    public void unregister(ClientInterface client)throws RemoteException;
    public void sendFile(FileTransfer file) throws RemoteException ;
    public AccountAccessOperationsInterface getAccessOperationsInstance()throws RemoteException;
    public UserInfoInterface getUserInfoInterface()throws RemoteException;
    public FriendRequestInterface getFriendRequestInstance() throws RemoteException;
    public void sendStatisticAlert() throws RemoteException;
    public void recieveNotification(String message) throws RemoteException;
    public void sendPrivateMessage(Message message) throws RemoteException;
    public void updateFriendsAlert(ClientInterface client,String key) throws RemoteException;
    public void sendGroupMessage(Message message , String gid , String gName , ArrayList<User> groupList) throws RemoteException;
}
