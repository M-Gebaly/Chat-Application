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
public interface UserInfoInterface extends Remote{
    public ArrayList<User> getFriendList(long id) throws RemoteException;
    public int [] getGenderNumbers() throws RemoteException;
    public int[] getStatusNumbers() throws RemoteException;
    public void updateStatus(long id,String status) throws RemoteException;
    public void updateStatusFlag(long id,String flag) throws RemoteException;

}

