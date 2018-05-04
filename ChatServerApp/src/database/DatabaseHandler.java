/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleDriver;

/**
 *
 * @author Asmaa
 */
public class DatabaseHandler {
    private static DatabaseHandler databaseHandler;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement pres;
   
    private DatabaseHandler() {
        connect();
       
    }
    public static DatabaseHandler getInstance(){
        if(databaseHandler==null)
            databaseHandler=new DatabaseHandler();
        
        return databaseHandler;
    }
    private void connect(){
        try {
            DriverManager.registerDriver(new OracleDriver());
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "chat", "chat");
            System.out.println("connection succeeded");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    public void insert(String query){
        try {
            pres = connection.prepareStatement(query);
            pres.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void update(String query){
        try {
            pres = connection.prepareStatement(query);
            pres.executeUpdate();
            //statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void delete(String query){
        try {
            pres = connection.prepareStatement(query);
            pres.executeUpdate();
            //statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ResultSet select(String query){
        
        try {
            pres = connection.prepareStatement(query);
            resultSet = pres.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
    /*public void closeConnection(){
        try {
            connection.close();
            databaseHandler=null;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
  
}
