/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.sun.deploy.security.ruleset.RunRule;
import controller.ClientImpl;
import controller.RmiFactory;
import interfaces.ClientInterface;
import interfaces.ServerInterface;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.Pair;
import model.*;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author M.Gebaly
 */
public class FXMLMainChatController implements Initializable {

    User user = null;
    User friendUser = null;
    ServerInterface server;
    ClientInterface clientImpl;
    ArrayList<User> frindList = new ArrayList<>();
    ArrayList<User> requests = new ArrayList<>();
    ArrayList<User> groupList = new ArrayList<>();
    ObservableList<User> oFrindList;
    ObservableList<User> oRequests;

    @FXML
    private Label userNameLable;

    @FXML
    private Label cFriendName;

    @FXML
    private Label cFriendstatuse;

    @FXML
    private ImageView cFriendimage;

    @FXML
    JFXListView<User> frindListView;
    @FXML
    JFXListView<User> requestListView;

    @FXML
    private TabPane tabPane;

    @FXML
    private Circle circleImage;

    @FXML
    private ComboBox comboBoxStatus;
    
    @FXML
    private BorderPane mainChatPanel;

    Map<String, Tab> tabsOpened = new HashMap<>();
    Map<String, FXMLChatContentController> tabsControllers = new HashMap<>();
    Map<String, Tab> GrouptabsOpened = new HashMap<>();
    Map<String, FXMLGroupChatContentController> GrouptabsControllers = new HashMap<>();
    ObservableList<String> statusList = FXCollections.observableArrayList("online", "away", "busy");

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        server = (ServerInterface) RmiFactory.getRmiInstance().connect();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage st = (Stage) userNameLable.getScene().getWindow();
                st.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        try {
                            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            server.unregister(clientImpl);
                            server.getUserInfoInterface().updateStatusFlag(user.getId(), "false");
                            Platform.exit();
                            System.exit(0);
                        } catch (RemoteException ex) {
                            Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("You Exit");
                    }
                });
                try {
                    clientImpl = new ClientImpl(user, FXMLMainChatController.this);
                    server.register(clientImpl);
                    server.getUserInfoInterface().updateStatusFlag(user.getId(), "true");
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                userNameLable.setText(user.getName());
                circleImage.setStroke(Color.SEAGREEN);
                Image profileImage;
                if (user.getGender().equals("male")) {
                    profileImage = new Image("resources/male.png");
                } else {
                    profileImage = new Image("resources/female.png");
                }
                circleImage.setFill(new ImagePattern(profileImage));
                circleImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                comboBoxStatus.setItems(statusList);
                comboBoxStatus.setValue(user.getStatus());

                loadFrindList();
                updateFriendsRequests();
            }
        });
    }

    public FXMLMainChatController() {
    }

    public void loadFrindList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                try {
                    frindList = server.getUserInfoInterface().getFriendList(user.getId());
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                oFrindList = FXCollections.observableArrayList(frindList);

                frindListView.setItems(oFrindList);
                frindListView.setCellFactory(listView -> new ListCell<User>() {

                    @Override
                    public void updateItem(User friend, boolean empty) {
                        super.updateItem(friend, empty);
                        if (empty || friend == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setGraphic(loadCell(friend));

                        }
                    }

                });
                //--- Action on click on any frind---------//
                frindListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        String userName = frindListView.getSelectionModel().getSelectedItem().getName();
                        String gender = frindListView.getSelectionModel().getSelectedItem().getGender();
                        String status = frindListView.getSelectionModel().getSelectedItem().getStatus();
                        friendUser = frindListView.getSelectionModel().getSelectedItem();
                        String friendFlage = frindListView.getSelectionModel().getSelectedItem().getStatusFlag();
                        System.out.println(userName);
                        if (friendFlage.equals("true")) {
                            cellClickAction(userName, friendUser.getEmail(), gender, status);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("This is Offline");
                            alert.showAndWait();
                        }

                    }
                });
            }
        });

    }

    Node loadCell(User friend) {
        ImageView imageView = new ImageView();
        ImageView imageViewStatus = new ImageView();

        FlowPane flow = new FlowPane();
        flow.setHgap(4);
        flow.setPrefWidth(1);

        Label friendName = new Label();
        friendName.setText(friend.getName());
        friendName.setStyle("-fx-font-size:20;-fx-font-family:\"Agency FB\";-fx-font-weight: bold;");

        Image image;

        if (friend.getGender().equals("female")) {
            image = new Image("/resources/female.png", true);
        } else {
            image = new Image("/resources/male.png", true);
        }

        Image statusImg = null;

        switch (friend.getStatus()) {
            case "away":

                statusImg = new Image("/resources/circle.png", true);
                break;
            case "avalable":
                statusImg = new Image("/resources/online.png", true);

                break;
            case "busy":

                statusImg = new Image("/resources/busy.png", true);
                break;
        }

        imageView.setImage(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        imageViewStatus.setImage(statusImg);
        imageViewStatus.setFitWidth(10);
        imageViewStatus.setFitHeight(10);

        flow.getChildren().addAll(imageView, friendName, imageViewStatus);
        return flow;
    }

    //--------------Function For Frinds Requests-----------------------------
    public void updateFriendsRequests() {

        try {
            requests = server.getFriendRequestInstance().getRequestsList(user.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        oRequests = FXCollections.observableArrayList(requests);
        if (oRequests != null) {
            requestListView.setItems(oRequests);
            requestListView.setCellFactory(listView -> new ListCell<User>() {

                Button btnAccept = new Button();
                Button btnIgnore = new Button();

                @Override
                public void updateItem(User name, boolean empty) {
                    super.updateItem(name, empty);

                    if (empty || name == null) {
                        setText(null);
                        setGraphic(null);
                    } else {

                        BorderPane pane = new BorderPane();

                        Label labelRequestFrom = new Label();
                        labelRequestFrom.setText(name.getName());
                        labelRequestFrom.setStyle("-fx-font-size:30;-fx-font-family:\"Agency FB\";-fx-font-weight: bold;");

                        btnAccept.setGraphic(new ImageView(new Image("/resources/accept.png", 20, 20, false, false)));
                        btnAccept.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    
                                    server.getFriendRequestInstance().acceptRequest(user.getId(), name.getId());
                                    server.updateFriendsAlert(clientImpl,"new");
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //update requests
                                updateFriendsRequests();
//
//                              //update list of friends
                                loadFrindList();
                            }
                        });
                        btnIgnore.setGraphic(new ImageView(new Image("/resources/ignore.png", 20, 20, false, false)));
                        btnIgnore.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    server.getFriendRequestInstance().rejectRequest(user.getId(), name.getId());
                                } catch (RemoteException ex) {
                                    Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                updateFriendsRequests();
                            }
                        });

                        HBox btnHbox = new HBox();

                        btnHbox.getChildren().addAll(btnIgnore, btnAccept);
                        btnHbox.setSpacing(4);
                        pane.setRight(btnHbox);
                        pane.setLeft(labelRequestFrom);
                        setGraphic(pane);

                    }
                }
            });
        } else {
            //requestsTab.setDisable(true);
        }
    }

    //----------------------------Add Frind--------------------------------
    @FXML
    void addFrindActon(MouseEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add New Friend");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtFieldEmail = new TextField();
        txtFieldEmail.setPromptText("username");

        grid.add(new Label("Email :"), 0, 0);
        grid.add(txtFieldEmail, 1, 0);

        dialog.getDialogPane().setStyle(" -fx-background-color: rgba(0, 0, 0, 0);");
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>("", txtFieldEmail.getText());
            }
            return null;
        });
        //dialog.getDialogPande().setBackground(Background.EMPTY);
        Platform.runLater(() -> txtFieldEmail.requestFocus());

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(check -> {
            if (check.getValue().equals(user.getEmail())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Infrom You");
                alert.setHeaderText("Infrom You");
                alert.setContentText("This is Your Email");
                alert.showAndWait();
            } else {
                boolean serverResult = false;
                try {
                    serverResult = server.getFriendRequestInstance().sendRequest(user.getId(), txtFieldEmail.getText());
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (serverResult) {
                    try {
                        //server.updateFriendsAlert(clientImpl, "status");
                        server.updateFriendsAlert(clientImpl, txtFieldEmail.getText());
                    } catch (RemoteException ex) {
                        Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Infrom You");
                    alert.setHeaderText("Infrom You");
                    alert.setContentText("You Send request successful");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Infrom You");
                    alert.setHeaderText("Infrom You");
                    alert.setContentText("This email may already frind with you or not in system");
                    alert.showAndWait();
                }
            }
        });

    }

    //------------------------------- create tabs ----------------------------//
    public void cellClickAction(String friendName, String email, String friendGender, String friendStatus) {
        if (!tabsOpened.containsKey(email)) {
            try {

                Tab newTab = new Tab();

                newTab.setId(email);
                newTab.setText(friendName);

                newTab.setClosable(true);
                cFriendName.setText(friendName);
                cFriendstatuse.setText(friendStatus);
                Image image;

                if (friendGender.equals("female")) {
                    image = new Image("/resources/female.png", true);
                } else {
                    image = new Image("/resources/male.png", true);
                }
                cFriendimage.setImage(image);

                newTab.setOnCloseRequest(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {

                        tabsOpened.remove(newTab.getId());
                        tabsControllers.remove(newTab.getId());
                    }
                });
                newTab.setOnSelectionChanged(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        cFriendName.setText(friendName);
                        cFriendstatuse.setText(friendStatus);
                        Image image;

                        if (friendGender.equals("female")) {
                            image = new Image("/resources/female.png", true);
                        } else {
                            image = new Image("/resources/male.png", true);
                        }
                        cFriendimage.setImage(image);
                    }
                });

                tabPane.getTabs().add(newTab);
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLChatContent.fxml"));
                FXMLChatContentController chatContentController = new FXMLChatContentController(); //receiver
                chatContentController.setUser(user);
                chatContentController.setFriendUser(friendUser);
                chatContentController.setFrindList(frindList);
                loader.setController(chatContentController);

                tabsOpened.put(email, newTab);
                tabsControllers.put(email, chatContentController);

                newTab.setContent(loader.load());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            tabPane.getSelectionModel().select(tabsOpened.get(email));
        }
    }

    public void GroupcellClickAction(String gname, String gid, String friendGender, String friendStatus) {
        if (!GrouptabsOpened.containsKey(gid)) {
            try {

                Tab newTab = new Tab();

                newTab.setId(gid);
                newTab.setText(gname);

                newTab.setClosable(true);
                cFriendName.setText(gname);
                cFriendstatuse.setText(friendStatus);
                Image image;

                if (friendGender.equals("female")) {
                    image = new Image("/resources/female.png", true);
                } else {
                    image = new Image("/resources/male.png", true);
                }
                cFriendimage.setImage(image);

                newTab.setOnCloseRequest(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {

                        GrouptabsOpened.remove(newTab.getId());
                        GrouptabsControllers.remove(newTab.getId());
                    }
                });
                newTab.setOnSelectionChanged(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        cFriendName.setText(gname);
                        cFriendstatuse.setText(friendStatus);
                        Image image;

                        if (friendGender.equals("female")) {
                            image = new Image("/resources/female.png", true);
                        } else {
                            image = new Image("/resources/male.png", true);
                        }
                        cFriendimage.setImage(image);
                    }
                });

                tabPane.getTabs().add(newTab);
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLGroupChatContent.fxml"));
                FXMLGroupChatContentController chatContentController = new FXMLGroupChatContentController(); //receiver
                chatContentController.setUser(user);
                chatContentController.setGroupList(groupList);
                chatContentController.setGroupId(gid);
                chatContentController.setGroupName(gname);
//                chatContentController.setFrindList(frindList);
                loader.setController(chatContentController);

                GrouptabsOpened.put(gid, newTab);
                GrouptabsControllers.put(gid, chatContentController);

                newTab.setContent(loader.load());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            tabPane.getSelectionModel().select(GrouptabsOpened.get(gid));
        }
    }    
    
    @FXML
    private void iconCreateGroupAction(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLGroup.fxml"));
            FXMLGroupController fXMLGroupController = new FXMLGroupController(this);
            fXMLGroupController.setUser(user);
            loader.setController(fXMLGroupController);
            Parent root = loader.load();
            //FXMLGroupController controller = loader.getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Create New Group");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
     //--------------------------LogOut-----------------------
     @FXML
     private void logoutAction(ActionEvent event){
        try {
            server.unregister(clientImpl);
            server.getUserInfoInterface().updateStatusFlag(user.getId(), "false");
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
                        server.sendStatisticAlert();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
                        Parent root = loader.load();
                        FXMLLoginController controller = loader.getController();
                        //controller.setEmail(remail.getText());
                        

                        Scene scene = new Scene(root);

                        Stage stage = (Stage) mainChatPanel.getScene().getWindow();
                        stage.setTitle("Login Page");
                        stage.setResizable(false);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
     }

    public ArrayList<User> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<User> groupList) {
        this.groupList = groupList;
    }

    public Map<String, Tab> getGrouptabsOpened() {
        return GrouptabsOpened;
    }

    public void setGrouptabsOpened(Map<String, Tab> GrouptabsOpened) {
        this.GrouptabsOpened = GrouptabsOpened;
    }     
    //---- Change Status--------------
    public void changeStatus() {
        try {
            server.getUserInfoInterface().updateStatus(user.getId(), comboBoxStatus.getValue().toString());
            server.updateFriendsAlert(clientImpl, "status");
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLMainChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //------- get User from Login Page
    public void setUser(User user) {
        this.user = user;
    }

    //-------- get controllers tab in recieve method
    public Map<String, FXMLChatContentController> getTabsControllers() {
        return tabsControllers;
    }

    //-------- get array list of users in the clientImpl
    public ArrayList<User> getFrindList() {
        return frindList;
    }

    
    public Map<String, FXMLGroupChatContentController> getGrouptabsControllers() {
        return GrouptabsControllers;
    }

    public void setGrouptabsControllers(Map<String, FXMLGroupChatContentController> GrouptabsControllers) {
        this.GrouptabsControllers = GrouptabsControllers;
    }    
    public void renderNotification(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Image logoImage = new Image("/resources/alarm-filled.png");
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Chat Application");
                tray.setMessage(message);
                tray.setRectangleFill(Paint.valueOf("#2962FF"));
                tray.setAnimationType(AnimationType.FADE);
                tray.setImage(logoImage);
                tray.showAndDismiss(Duration.seconds(4));
            }
        });

    }

}
