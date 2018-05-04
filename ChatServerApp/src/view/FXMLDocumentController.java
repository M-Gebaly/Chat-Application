/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import controller.*;
import interfaces.FactoryInterface;
import interfaces.ServerInterface;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Asmaa
 */
public class FXMLDocumentController implements Initializable {

    private ServerImpl serverImpl;
    private Registry registry;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXTextArea txtNotification;
    @FXML
    private Label label;
    @FXML
    private Button startbtn;
    @FXML
    private Button endbtn;
    @FXML
    ServerInterface server;

    @FXML
    private PieChart testChart;

    @FXML
    private PieChart statusChart;
    @FXML
    private Label male;
    @FXML
    private Label female;
    @FXML
    private Label online;
    @FXML
    private Label offline;
    @FXML
    private Label total;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            endbtn.setDisable(false);
            if (registry == null) {
                registry = LocateRegistry.createRegistry(2000);
            }

            serverImpl = new ServerImpl();
            serverImpl.setController(this);
            registry.rebind("chat", serverImpl);
            label.setText("Started");
            startbtn.setDisable(true);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleEndButtonAction(ActionEvent event) {
        //sop("handleEndButtonAction .. before try ")
        try {

            startbtn.setDisable(false);
            registry.unbind("chat");
            label.setText(" Ended");
            endbtn.setDisable(true);
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage st = (Stage) anchorPane.getScene().getWindow();
                st.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        
                        Platform.exit();
                        System.exit(0);
                        System.out.println("You Exit");
                    }
                });
            }
        });
    }

    public void sendNotificationAction(ActionEvent actionEvent) {
        try {
            System.out.println("sendNotificationAction: " + txtNotification.getText());
            serverImpl.recieveNotification(txtNotification.getText());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleChartAction(Event event) {
        getChart();
    }

    public void getChart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("hiii again");
                    FactoryInterface factory = new FactoryImpl();
                    int[] genders = factory.getUserInfoInstance().getGenderNumbers();
                    int all = genders[0] + genders[1];
                    float m = genders[0] / (float) all;
                    float f = genders[1] / (float) all;
                    male.setText(String.format("%.0f", m * 100) + "%");
                    female.setText(String.format("%.0f", f * 100) + "%");
                    total.setText(String.valueOf(all));
                    /*IntegerProperty male=new SimpleIntegerProperty();
                    IntegerProperty female=new SimpleIntegerProperty();
                    male.add(genders[0]);
                    female.add(genders[1]);*/
                    ObservableList<PieChart.Data> pieChartData
                            = FXCollections.observableArrayList(
                                    new PieChart.Data("Male", genders[0]),
                                    new PieChart.Data("Female", genders[1]));

                    testChart.setData(pieChartData);
                    testChart.setLegendVisible(true);

                    applyCustomColorSequence(
                            pieChartData,
                            "#9BD8E6",
                            "#FF7C9C"
                    );

                    int status[] = factory.getUserInfoInstance().getStatusNumbers();
                    online.setText(String.valueOf((status[0])));
                    offline.setText(String.valueOf((status[1])));
                    ObservableList<PieChart.Data> pieChart
                            = FXCollections.observableArrayList(
                                    new PieChart.Data("Online", status[0]),
                                    new PieChart.Data("Offline", status[1]));
                    statusChart.setData(pieChart);
                    applyCustomColorSequence(
                            pieChart,
                            "#bbee9b",
                            "#c9c9c8"
                    );
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
            i++;
        }
    }

}
