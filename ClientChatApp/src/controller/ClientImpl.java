/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import interfaces.ClientInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Message;
import model.User;
import view.FXMLMainChatController;

/**
 *
 * @author Asmaa
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface{
    User user;
    User friendUser;
    FXMLMainChatController fXMLMainChatController;

    public ClientImpl(User user, FXMLMainChatController fXMLMainChatController) throws RemoteException{
        this.user = user;
        this.fXMLMainChatController = fXMLMainChatController;
    }
    
    public ClientImpl(User user) throws RemoteException {
        this.user = user;
    }

    @Override
    public User getUser() throws RemoteException{
        return user;
    }

    public void setUser(User user) throws RemoteException{
        this.user = user;
    }
    
    @Override
    public void recieve(Message message) throws RemoteException {
        System.out.println("from recieve");
        //
        //MainController.MainChatController.getMainChatControllerInstance().getFXMLMainChatControllerInstance().cellClickAction("bnbn", message.getFrom());
        //MainController.ChatContentController.getChatContentControllerInstance().getFXMLChatContentControllerInstance().render(message);
        //MainController.MainChatController.getMainChatControllerInstance().getFXMLMainChatControllerInstance().tabsControllers.get(message.getFrom()).render(message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (User  fUser:fXMLMainChatController.getFrindList())
                {
                    if (fUser.getEmail().equals(message.getFromEmail()))
                    {
                    friendUser = fUser;
                    }
                } 
                fXMLMainChatController.cellClickAction(friendUser.getName(), message.getFromEmail() , friendUser.getGender(),friendUser.getStatus());
                fXMLMainChatController.getTabsControllers().get(message.getFromEmail()).render(message);           
            }
        });

        
        System.out.println(message);
    }    

    @Override
    public void recieveNotification(String message) throws RemoteException {
        fXMLMainChatController.renderNotification(message);

    }
    @Override
    public void refreshContacts() throws RemoteException {
        fXMLMainChatController.loadFrindList();
    }
    
   @Override
    public void reciveFile(String filename, byte[] data, int dataLength) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("receive File");
                    alert.setHeaderText("Are you sure want to move this file to c:/?");
                     Optional<ButtonType> option = alert.showAndWait();


                   if (option.get() == ButtonType.OK) {

                    try {

                        System.out.println("Done writing data...");
                        String pathDefault = "C:\\Users\\Public\\Downloads\\";
                        File f = new File(pathDefault + filename);
                        f.createNewFile();
                        FileOutputStream out = new FileOutputStream(f, true);
                        out.write(data, 0, dataLength);
                        out.flush();
                        out.close();
                        System.out.println("Done writing data...");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }           
            }
        });

       
    }
    
    @Override
    public void refreshRequests() throws RemoteException {
        fXMLMainChatController.updateFriendsRequests();
    }
 
    @Override
    public void recieveGroupMessage(Message message, String gid, String gName, ArrayList<User> groupList) throws RemoteException {
        System.out.println("from recieve");
        //groupList.remove(user);
        int i = 0;
        for (User user1 : groupList) 
        {
            if (user.getEmail().equals(user1.getEmail())) 
            {
                break;
            }
            i++;
        }
        groupList.remove(i);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println(user.getName());
                
                for (User  fUser:fXMLMainChatController.getFrindList())
                {
                    if (fUser.getEmail().equals(message.getFromEmail()))
                    {
                        groupList.add(fUser);
                    }
                }
                
                fXMLMainChatController.GroupcellClickAction(gName, gid, user.getGender(), user.getStatus());
                fXMLMainChatController.getGrouptabsControllers().get(gid).setGroupId(gid);
                fXMLMainChatController.getGrouptabsControllers().get(gid).setGroupName(gName);
                fXMLMainChatController.getGrouptabsControllers().get(gid).setGroupList(groupList);
                fXMLMainChatController.getGrouptabsControllers().get(gid).render(message);           
            }
        });

        
        System.out.println(message);        
    }    
}

