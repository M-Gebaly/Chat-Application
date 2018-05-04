/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXTextField;
import controller.RmiFactory;
import interfaces.ServerInterface;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author M.Gebaly
 */
public class FXMLConnectToServerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ServerInterface server;
    @FXML
    private JFXTextField ipText;

    @FXML
    private Label invalidLabel;

    @FXML
    private AnchorPane connServerPane;

    private static final String IP_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    Pattern pattern = Pattern.compile(IP_PATTERN);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage st = (Stage) connServerPane.getScene().getWindow();
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
    private void connectAction(ActionEvent event) {
        String IP = ipText.getText();
        Matcher matcher = pattern.matcher(IP);

        if (!matcher.matches()) {
            invalidLabel.setVisible(true);
        } else {
            invalidLabel.setVisible(false);
            RmiFactory.getRmiInstance().setIp(IP);
            server = (ServerInterface) RmiFactory.getRmiInstance().connect();
            if (RmiFactory.getRmiInstance().isConnected()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
                    Parent root = loader.load();
                    FXMLLoginController controller = loader.getController();
                    //controller.setEmail(remail.getText());

                    Scene scene = new Scene(root);

                    Stage stage = (Stage) connServerPane.getScene().getWindow();
                    stage.setTitle("Login Page");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLConnectToServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                invalidLabel.setVisible(true);
            }

        }
    }
}
