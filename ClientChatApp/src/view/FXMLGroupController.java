/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import controller.RmiFactory;
import interfaces.ServerInterface;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import model.*;

/**
 * FXML Controller class
 *
 * @author M.Gebaly
 */
public class FXMLGroupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private FXMLMainChatController fXMLMainChatController;
    @FXML
    private JFXTextField txtFieldGroupName;
    @FXML
    private JFXListView<User> listviewGroup;
    @FXML
    private JFXButton btnCreate;
    
    ArrayList<String> groupMembers = new ArrayList<>();
    ArrayList<User> selectedUsers  = new ArrayList<>();
    Random Rand = new Random();
    ServerInterface server;
    User user = null;

    public FXMLGroupController(FXMLMainChatController fXMLMainChatController) {
        this.fXMLMainChatController = fXMLMainChatController;
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        server = (ServerInterface) RmiFactory.getRmiInstance().connect();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                loadFrindList();
            }
        });
        
    }

    void loadFrindList() {
        ArrayList<User> friendsList = null;
        ArrayList<User> contactsName = new ArrayList<>();
        
        try {
            friendsList = server.getUserInfoInterface().getFriendList(user.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGroupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (friendsList != null) {
            for (User contact : friendsList) {
                if(contact.getStatusFlag().equals("true")){
                    contactsName.add(contact);
                }
                
            }
        }
        ObservableList<User> data = FXCollections.observableArrayList(contactsName);
        listviewGroup.setItems(data);
        listviewGroup.setCellFactory(listView -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    
                    setText(null);
                    setGraphic(null);
                    
                } else {
                    JFXCheckBox checkbox = new JFXCheckBox(item.getName());
                    checkbox.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (!groupMembers.contains(checkbox.getText())) {
                                groupMembers.add(checkbox.getText());
                                System.out.println(groupMembers.get(0));
                                selectedUsers.add(item);
                                System.out.println(selectedUsers.get(0).getName());
                            } else {
                                groupMembers.remove(checkbox.getText());
                                selectedUsers.remove(item);
                            }
                        }
                    });
                    setGraphic(checkbox);
                }
            }
        });
        
        btnCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                fXMLMainChatController.setGroupList(selectedUsers);
                fXMLMainChatController.GroupcellClickAction(txtFieldGroupName.getText(), Rand.nextInt(10000)+"", user.getGender(), user.getStatus());
                Stage stage = (Stage) btnCreate.getScene().getWindow();
                stage.close();
            }
        });
    }
    
    public void setUser(User user) {
        this.user = user;
    } 
    
}
