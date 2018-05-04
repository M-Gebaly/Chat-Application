/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import controller.RmiFactory;
import interfaces.ServerInterface;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;

/**
 * FXML Controller class
 *
 * @author M.Gebaly
 */
public class FXMLRegistrationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    ServerInterface server;
    
    private static final String EMAIL_PATTERN  = "^[_A-Za-z0-9-.]+([A-Za-z0-9-_.]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            
    @FXML
    private AnchorPane signupPane;
    
    @FXML
    private JFXTextField rname;

    @FXML
    private JFXPasswordField rpassword;

    @FXML
    private JFXTextField remail;
    
    @FXML
    private JFXPasswordField apassword;

    @FXML
    private JFXRadioButton male;

    @FXML
    private JFXRadioButton female;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        server = (ServerInterface) RmiFactory.getRmiInstance().connect();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage st = (Stage) signupPane.getScene().getWindow();
                st.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                        System.out.println("You Exit");
                    }
                });
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                apassword.focusedProperty().addListener(new ChangeListener<Boolean>(){
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        if (!newValue) {
                            String Password = rpassword.getText();
                            String password_2 = apassword.getText();
                            if (!password_2.equals(Password)) {
                                apassword.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n"
                                        + "    -fx-text-fill: #fff;");
                                apassword.setPromptText("Password not matched");
                            }else{
                                apassword.setStyle("-jfx-focus-color: #a0a2ab;-fx-prompt-text-fill: #4059a9;\n"
                                        + "    -fx-text-fill:  #a0a2ab");
                                apassword.setPromptText("Password matched");
                            }
                        }
                    }
                    
                });
            }
        });
    }


 @FXML
    private void backLogin(ActionEvent event){
        try {
            server.sendStatisticAlert();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLLogin.fxml"));
            Parent root = loader.load();
            FXMLLoginController controller = loader.getController();
            //controller.setText(nameTxtField.getText());

            Scene scene = new Scene(root);
            
            Stage stage = (Stage) signupPane.getScene().getWindow();
            stage.setTitle("Login Page");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
//    @FXML
//    private  void passwordReleased(ActionEvent event){
//        String password_1 = rpassword.getText();
//        String password_2 = opassword.getText();
//        
//        if(!password_2.equals(password_1)){
//            opassword.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
//"    -fx-text-fill: #fff;");
//            opassword.setPromptText("Password not matched");
//        }
//    }
    
    @FXML
    private void signupAction(ActionEvent event){
        String name = rname.getText();
        String Password = rpassword.getText();
        String password_2 = apassword.getText();
        String email = remail.getText();

        boolean isMale = male.isSelected();
        boolean isFemale = female.isSelected();
        Matcher matcher = pattern.matcher(email);
        
        
        if (name.equals("") || Password.equals("") || email.equals("") || password_2.equals("")) {

            if (name.equals("")) {
                rname.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
"    -fx-text-fill: #fff;");
            }
            if (Password.equals("")) {
                rpassword.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
"    -fx-text-fill: #fff;");
                rpassword.setPromptText("********");
            }
            if (email.equals("")) {
                remail.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
"    -fx-text-fill: #fff;");
            }
            remail.setPromptText("abc@abc.com");

        } else if (!password_2.equals(Password)) {
            apassword.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n"
                    + "    -fx-text-fill: #fff;");
            apassword.setPromptText("Password not matched");
        } else if (!matcher.matches()) {
            remail.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n"
                    + "    -fx-text-fill: #fff;");
            remail.setPromptText("acz@xxxx.com");
        } else {
            User user = new User();
            user.setName(name);
            user.setPassword(Password);
            user.setEmail(email);
            if (isMale) {
                user.setGender("male");
            }
            if (isFemale) {
                user.setGender("female");
            }
            boolean result;
            try {
                result = server.getAccessOperationsInstance().signUp(user);
                server.sendStatisticAlert();
                if (!result) {
                    
                } else {
                    try {
                        server.sendStatisticAlert();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
                        Parent root = loader.load();
                        FXMLLoginController controller = loader.getController();
                        //controller.setEmail(remail.getText());
                        

                        Scene scene = new Scene(root);

                        Stage stage = (Stage) signupPane.getScene().getWindow();
                        stage.setTitle("Login Page");
                        stage.setResizable(false);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLRegistrationController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}
