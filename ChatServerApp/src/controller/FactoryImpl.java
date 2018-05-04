/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import interfaces.AccountAccessOperationsInterface;
import interfaces.FactoryInterface;
import interfaces.FriendRequestInterface;
import interfaces.UserInfoInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Asmaa
 */
public class FactoryImpl extends UnicastRemoteObject implements FactoryInterface{

    public FactoryImpl() throws RemoteException{
    }
    
    
    @Override
    public AccountAccessOperationsInterface getAAOInstance() throws RemoteException{
        AccountAccessOperationsInterface aao=new AccountAccessOperationsImpl();
        return aao;
    }

    @Override
    public UserInfoInterface getUserInfoInstance() throws RemoteException{
        UserInfoInterface userInfoInterface=new UserInfoImpl();
        return userInfoInterface;
    }
    @Override
    public FriendRequestInterface getFriendRequestInstance() throws RemoteException {
        FriendRequestInterface friendRequestInterface=new FriendRequestImpl();
        return friendRequestInterface;
    }
    
}
