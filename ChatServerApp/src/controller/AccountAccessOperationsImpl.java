/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import interfaces.AccountAccessOperationsInterface;
import model.User;
import database.UserTableOperations;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Asmaa
 */
public class AccountAccessOperationsImpl extends UnicastRemoteObject implements AccountAccessOperationsInterface{

    public AccountAccessOperationsImpl() throws RemoteException{
    }

    
    @Override
    public User login(String email, String password) throws RemoteException{

        return new UserTableOperations().loginHandler(email, password);
    }

    @Override
    public boolean signUp(User user) throws RemoteException{
        return new UserTableOperations().signUpHandler(user);
    }
    
}
