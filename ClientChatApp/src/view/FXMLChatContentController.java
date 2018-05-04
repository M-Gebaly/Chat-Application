/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXComboBox;
import controller.HistoryImpl;
import controller.HistoryInterface;
import controller.RmiFactory;
import interfaces.ServerInterface;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import model.FileTransfer;
import model.Message;
import model.User;

/**
 * FXML Controller class
 *
 * @author M.Gebaly
 */
public class FXMLChatContentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private User user = null;
    private ArrayList<Message>messagesList;
    private Message message = null;
    private User friendUser = null;
    private ArrayList<User> frindList;
    private ServerInterface server; 
    private String fontFamily;
    private String size;
    private String color;
    private HistoryInterface historyInterface;
    @FXML
    private JFXComboBox fontComboBox;
    
    @FXML
    private JFXComboBox fontSizeComboBox;
    
    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private TextField message_txt;
    @FXML
    private VBox chatArea;   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                historyInterface=new HistoryImpl();
                messagesList=new ArrayList<>();
                server = (ServerInterface) RmiFactory.getRmiInstance().connect();
                message_txt.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.getCode().equals(KeyCode.ENTER))
                        {
                            try {

                                if (friendUser != null) 
                                {
                                    Message message = new Message();
                                    message.setFromEmail(user.getEmail());
                                    message.setContent(message_txt.getText());
                                    message.setFontColor(color);
                                    message.setFontFamily(fontFamily);
                                    message.setGender(user.getEmail());
                                    message.setStatus(user.getStatus());
                                    message.setFontSize(Integer.parseInt(size));
                                    message.setToEmail(friendUser.getEmail());
                                    message.setFrom(user.getName());
                                    message.setTo(friendUser.getName());
                                    message.setFromID(user.getId());                                   
                                    message.setDate(getDateNow());
                                    messagesList.add(message);
                                    renderCurrentUser(message);
                                    server.sendPrivateMessage(message);                                   
                                }
                                else
                                {
                                    for (User  fUser: frindList) 
                                    {
                                        if (fUser.getEmail().equals(message.getFromEmail()))
                                        {
                                            friendUser = fUser;
                                        }
                                    }
                                    Message message = new Message();
                                    message.setFromEmail(user.getEmail());
                                    message.setContent(message_txt.getText());
                                    message.setFontColor(color);
                                    message.setFontFamily(fontFamily);
                                    message.setGender(user.getEmail());
                                    message.setStatus(user.getStatus());                                    
                                    message.setFontSize(Integer.parseInt(size));                                    
                                    message.setToEmail(friendUser.getEmail());
                                    message.setFrom(user.getName());
                                    message.setTo(friendUser.getName());
                                    message.setFromID(user.getId());                                   
                                    message.setDate(getDateNow());
                                    messagesList.add(message);
                                    renderCurrentUser(message);
                                    server.sendPrivateMessage(message);                                    
                                }
                                
                                message_txt.setText("");
                            } catch (RemoteException ex) {

                                Logger.getLogger(FXMLChatContentController.class.getName()).log(Level.SEVERE, null, ex);
                            }                    
                        }
                    }     
                });
                 
                 fontComboBox.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        fontFamily = fontComboBox.getSelectionModel().getSelectedItem().toString();
                        System.out.println(oldValue);
                        System.out.println(newValue);
                    }
                });
                
                fontSizeComboBox.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        size = fontSizeComboBox.getSelectionModel().getSelectedItem().toString();
                        System.out.println(oldValue);
                        System.out.println(newValue);
                    }
                });
                colorPicker.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        color = toRGBCode(colorPicker.getValue());
                    }
                });
            }
        });
        customizeEditorPane();
    }    
    void customizeEditorPane() {
        ObservableList<String> limitedFonts = FXCollections.observableArrayList("Arial", "Times", "Courier New", "Comic Sans MS");
        fontComboBox.setItems(limitedFonts);
        fontComboBox.getSelectionModel().selectFirst();

        ObservableList<String> fontSizes = FXCollections.observableArrayList("8", "10", "12", "14", "18", "24");
        fontSizeComboBox.setItems(fontSizes);
        fontSizeComboBox.getSelectionModel().select(2);

        colorPicker.setValue(Color.BLACK);
    }
    @FXML
    void messageStyle(MouseEvent event){
        fontFamily = fontComboBox.getSelectionModel().getSelectedItem().toString();
        size = fontSizeComboBox.getSelectionModel().getSelectedItem().toString();
        color = toRGBCode(colorPicker.getValue());
        message_txt.setStyle("-fx-font-family:\""+fontFamily+"\";"
                + "-fx-background-radius:"+40+";"
                        + "-fx-font-size:"+size+";"
                                + "-fx-text-fill:"+color+";");
    }
    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


    public void renderCurrentUser (Message message)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox hBox = new HBox();
                Label messageLabel = new Label(message.getContent());
                messageLabel.setStyle("-fx-background-radius: 0 20 20 20; -fx-font: "+message.getFontSize()+" \""+message.getFontFamily()+"\"; -fx-padding: 10px; -fx-background-color:  #0000FF; -fx-text-fill:"+message.getFontColor()+";");
                hBox.getChildren().addAll(messageLabel);
                hBox.setAlignment(Pos.TOP_LEFT);
                chatArea.setStyle("-fx-spacing: 5px;");
                chatArea.getChildren().add(hBox);                
            }
        });
    }
    public void render(Message messageData) {
        this.message = messageData;
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messagesList.add(message);               
                HBox hBox = new HBox();
                Label messageLabel = new Label(messageData.getContent());
                messageLabel.setStyle("-fx-background-radius: 20 0 20 20; -fx-font: "+messageData.getFontSize()+" \""+messageData.getFontFamily()+"\"; -fx-padding: 10px; -fx-background-color:  #d3d3d3; -fx-text-fill:"+messageData.getFontColor()+";");
                hBox.getChildren().addAll(messageLabel);
                hBox.setAlignment(Pos.TOP_RIGHT);
                chatArea.setStyle("-fx-spacing: 5px;");
                chatArea.getChildren().addAll(hBox);     
                
                
                }
        });

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    public void setFrindList(ArrayList<User> frindList) {
        this.frindList = frindList;
    }
    
    public void handleSendButton(ActionEvent event){
         FileChooser chooser = new FileChooser();
         chooser.setTitle("Open File");
         File file = chooser.showOpenDialog(new Stage());
         FileTransfer ftransfer = new FileTransfer ();
         ftransfer.setFile(file);
         ftransfer.setFrom(user.getEmail());
         ftransfer.setTo(friendUser.getEmail());

        try {
            server.sendFile(ftransfer);
            System.out.println("in controller after send function");
        } catch (RemoteException ex) {
            ex.printStackTrace();
          //  Logger.getLogger(FXMLChatContentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void saveBtnAction(ActionEvent event) {
        Platform.runLater(() -> {

            Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            //Set extension filter
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml")
            );
            //Show save file dialog
            File file = fileChooser.showSaveDialog(st);

            historyInterface.saveHistory(messagesList, file, user.getId());

        });
    }
    public XMLGregorianCalendar getDateNow() {
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            return now;
        } catch (DatatypeConfigurationException ex) {
            ex.printStackTrace();
        }
        return null;
    }
  

}
