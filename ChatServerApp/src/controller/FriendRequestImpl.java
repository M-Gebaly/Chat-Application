/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.FriendTableOperation;
import interfaces.FriendRequestInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import model.User;

/**
 *
 * @author Asmaa
 */
public class FriendRequestImpl extends UnicastRemoteObject implements FriendRequestInterface{
    public FriendRequestImpl() throws RemoteException{
    }
    @Override
    public boolean sendRequest(long id,String friendEmail) throws RemoteException {
        return new FriendTableOperation().addFriendHandler(id, friendEmail);
    }

    @Override
    public ArrayList<User> getRequestsList(long id) throws RemoteException {
        return new FriendTableOperation().requestListHandler(id);
    }

    @Override
    public void acceptRequest(long userID, long friendID) throws RemoteException {
       new FriendTableOperation().acceptRequestHandler(userID, friendID);
    }

    @Override
    public void rejectRequest(long userID, long friendID) throws RemoteException {
        new FriendTableOperation().rejectRequestHandler(userID, friendID);
    }
    
}
