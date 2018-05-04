/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import interfaces.AccountAccessOperationsInterface;
import interfaces.ClientInterface;
import interfaces.FriendRequestInterface;
import interfaces.UserInfoInterface;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import model.FileTransfer;
import model.Message;
import model.User;
import view.FXMLDocumentController;

/**
 *
 * @author Asmaa
 */
public class ServerImpl extends UnicastRemoteObject implements interfaces.ServerInterface {

    FXMLDocumentController controller;
    public static ArrayList<ClientInterface> clients = new ArrayList<>();

    public ServerImpl() throws RemoteException {
    }

    @Override
    public AccountAccessOperationsInterface getAccessOperationsInstance() throws RemoteException {
        return new FactoryImpl().getAAOInstance();
    }

    @Override
    public UserInfoInterface getUserInfoInterface() throws RemoteException {
        return new FactoryImpl().getUserInfoInstance();
    }

    private void notifyFriendStatus(ClientInterface client, String msg) throws RemoteException {
        ArrayList<User> friends = new FactoryImpl().getUserInfoInstance().getFriendList(client.getUser().getId());
        for (User user : friends) {
            for (ClientInterface c : clients) {
                if (user.getId() == c.getUser().getId()) {
                    //c.recieveNotification(client.getUser().getName() + " is " + msg);
                    c.recieveNotification(client.getUser().getName()+msg);
                    c.refreshContacts();
                    System.out.println("controller.ServerImpl.register(): notificat");
                }
            }
        }
    }

    @Override
    public void register(ClientInterface client) throws RemoteException {
        System.out.println("from register");

        clients.add(client);
        //notifyFriendStatus(client, client.getUser().getStatus());
        notifyFriendStatus(client," is Online now");

        //client.recieveNotification(client.getUser().getName()+" is "+client.getUser().getStatus());
    }

    @Override
    public void unregister(ClientInterface client) throws RemoteException {
        System.out.println("from unregister");
        //notifyFriendStatus(client, "Offline");
        notifyFriendStatus(client," is Offline now");
        clients.remove(client);
    }

    @Override
    public FriendRequestInterface getFriendRequestInstance() throws RemoteException {
        return new FactoryImpl().getFriendRequestInstance();
    }

    @Override
    public void sendStatisticAlert() throws RemoteException {
        controller.getChart();
    }

    public void setController(FXMLDocumentController controller) {
        this.controller = controller;
    }

    @Override
    public void recieveNotification(String message) throws RemoteException {
        for (ClientInterface client : clients) {
            client.recieveNotification(message);
        }
    }

    @Override
    public void sendPrivateMessage(Message message) throws RemoteException {
        for (ClientInterface client : clients) {
            System.out.println(client.getUser().getEmail());
            if (client.getUser().getEmail().equals(message.getToEmail())) {
                client.recieve(message);
            }
        }
    }

    @Override
    public void sendFile(FileTransfer file) throws RemoteException {
        //send []bytes to client
        try {

            FileInputStream in = new FileInputStream(file.getFile());
            byte[] mydata = new byte[1024 * 1024];
            int mylen = in.read(mydata);
            System.out.println("before call recieve function in server");

            /*                             for(ClientInterface client : clients){
                             client.reciveFile(file.getFile().getName(),mydata,mylen);
                             }*/
            for (ClientInterface client : clients) {
                System.out.println(client.getUser().getEmail());
                if (client.getUser().getEmail().equals(file.getTo())) {
                    client.reciveFile(file.getFile().getName(), mydata, mylen);
                }
            }
            mylen = in.read(mydata);
            System.out.println("after call recieve function in server");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void updateFriendsAlert(ClientInterface client,String key) throws RemoteException {
       if(key.equalsIgnoreCase("status")){
           notifyFriendStatus(client, " update status to "+client.getUser().getStatus());
       }
       else if( key.equalsIgnoreCase("new")){
           notifyFriendStatus(client, " update his status to ");
       
       
       }
       else{ 
           for(ClientInterface c:clients){
               if((c.getUser().getEmail()).equalsIgnoreCase(key)){
                   c.recieveNotification(client.getUser().getName()+" sends you a friend request");
                   c.refreshRequests();
               }
           }
       }
       
    }

    @Override
    public void sendGroupMessage(Message message, String gid, String gName, ArrayList<User> groupList) throws RemoteException {
        System.out.println("from send group ");
        ArrayList<ClientInterface> groupClients = new ArrayList<>();
        for (User groupMember : groupList) 
        {
            System.out.println(groupMember.getName());
            for (ClientInterface client : clients) 
            {
                if (client.getUser().getEmail().equals(groupMember.getEmail())) 
                {
                    groupClients.add(client);
                    System.out.println(client.getUser().getName());
                }
            }
        }
        for (ClientInterface client : groupClients) 
        {
            client.recieveGroupMessage(message , gid , gName , groupList);   
        }        
    }
}
