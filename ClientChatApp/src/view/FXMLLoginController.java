/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.RmiFactory;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;
import interfaces.ServerInterface;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

/**
 *
 * @author M.Gebaly
 */
public class FXMLLoginController implements Initializable {

    ServerInterface server;

    @FXML
    private JFXTextField lemail;

    @FXML
    private JFXPasswordField lpassword;
    
    @FXML
    private AnchorPane loginPane;
    
    @FXML
    private Label errolable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        server = (ServerInterface) RmiFactory.getRmiInstance().connect();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage st = (Stage) loginPane.getScene().getWindow();
                st.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                        System.out.println("You Exit");
                    }
                });
            }
        });
    }

    @FXML
    private void loginAction(ActionEvent event) {
        String emailv = lemail.getText();
        String passwordv = lpassword.getText();
        if (!emailv.equals("") && !passwordv.equals("")) {
            User user = null;
            try {
                user = server.getAccessOperationsInstance().login(emailv, passwordv);
                server.sendStatisticAlert();
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user != null) {
                errolable.setVisible(false);
                    try {
                        server.sendStatisticAlert();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLMainChat.fxml"));
                        Parent root = loader.load();
                        FXMLMainChatController controller = loader.getController();
                        //controller.setEmail(remail.getText());
                        controller.setUser(user);

                        Scene scene = new Scene(root);

                        Stage stage = (Stage) loginPane.getScene().getWindow();
                        stage.setTitle("Chat Page");
                        stage.setResizable(false);
                        //stage.setOnCloseRequest(value);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                //System.err.println("vv");
            } else {
                //System.err.println("vv");
                errolable.setVisible(true);
            }
        } else {

            if (emailv.equals("")) {
                lemail.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
"    -fx-text-fill: #fff;");
                lemail.setPromptText("abc@abc.com");
                errolable.setVisible(false);
            }
            if (passwordv.equals("")) {
                lpassword.setStyle("-jfx-focus-color: #ee2e58;-fx-prompt-text-fill: #ee2e58;\n" +
"    -fx-text-fill: #fff;");
                lpassword.setPromptText("********");
                errolable.setVisible(false);
            }
            

        }
    }
    
    @FXML
    private void signupAction(ActionEvent event){
        errolable.setVisible(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLRegistration.fxml"));
            Parent root = loader.load();
            FXMLRegistrationController controller = loader.getController();
            //controller.setText(nameTxtField.getText());

            Scene scene = new Scene(root);
            
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.setTitle("Register Page");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 

}
