/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.FriendTableOperation;
import database.UserTableOperations;
import interfaces.UserInfoInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import model.User;

/**
 *
 * @author Asmaa
 */
public class UserInfoImpl extends UnicastRemoteObject implements UserInfoInterface{

    public UserInfoImpl() throws RemoteException{
    }

    
    @Override
    public ArrayList<User> getFriendList(long id) throws RemoteException{
        return new FriendTableOperation().friendListHandler(id);
    }

    @Override
    public int[] getGenderNumbers() throws RemoteException{
        return new UserTableOperations().genderNumbersHandler();
    }

    @Override
    public int[] getStatusNumbers() throws RemoteException {
        return new UserTableOperations().statusNumbersHandler();
    }
    @Override
    public void updateStatus(long id,String status) throws RemoteException {
        new UserTableOperations().statusHandler(id, status);
    }

    @Override
    public void updateStatusFlag(long id,String flag) throws RemoteException {
        new UserTableOperations().statusFlagHandler(id, flag);
    }
    
    
}
