/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Asmaa
 */
public interface FactoryInterface extends Remote{
      public AccountAccessOperationsInterface getAAOInstance() throws RemoteException;
      public UserInfoInterface getUserInfoInstance() throws RemoteException;
      public FriendRequestInterface getFriendRequestInstance() throws RemoteException;
}
