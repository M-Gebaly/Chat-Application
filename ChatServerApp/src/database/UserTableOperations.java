/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.spi.ImageTranscoderSpi;

/**
 *
 * @author Asmaa
 */

public class UserTableOperations {
 
    public boolean signUpHandler(User user){
        String query = "select email from Users where email = '" + user.getEmail() + "'";
        try {
            if((DatabaseHandler.getInstance().select(query)).next()){
                return false;
            }
            else{
                String sql = "INSERT INTO users(NAME,EMAIL,PASSWORD,GENDER,STATUS,STATUSFLAG) "
                        + "VALUES ('"+user.getName()+"','"+user.getEmail()+"','"+user.getPassword()+"','"+user.getGender()
                        +"','"+user.getStatus()+"','"+user.getStatusFlag()+"')";
                DatabaseHandler.getInstance().insert(sql);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserTableOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public User loginHandler(String email, String password){
        User user=null;
        String query="Select * from "
                +DatabaseTables.UserTable.tableName
                +" where "+DatabaseTables.UserTable.emailColumn+" = '" + email 
                + "' and "+DatabaseTables.UserTable.passwordColumn+" = '" + password + "'";
         boolean unique = false;
         ResultSet resultSet=DatabaseHandler.getInstance().select(query);
        try {
            while (resultSet.next()) {
                if (resultSet.getString(DatabaseTables.UserTable.emailColumn).equalsIgnoreCase(email)) {
                    unique = true;
                    break;
                }
            }
            if (unique) {
                user=new User(resultSet.getLong(DatabaseTables.UserTable.idColumn),
                        resultSet.getString(DatabaseTables.UserTable.nameColumn),
                        resultSet.getString(DatabaseTables.UserTable.emailColumn),
                        resultSet.getString(DatabaseTables.UserTable.passwordColumn),
                        resultSet.getString(DatabaseTables.UserTable.genderColumn),
                        resultSet.getString(DatabaseTables.UserTable.statusColumn),
                        resultSet.getString(DatabaseTables.UserTable.statusFlagColumn)
                );
            }
            resultSet.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserTableOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }
     public int[] genderNumbersHandler(){
         //0 male 1 female
            String query="select "+DatabaseTables.UserTable.genderColumn
                    +" from "+DatabaseTables.UserTable.tableName;
            int[] genders=new int[2];
            
            ResultSet resultSet=DatabaseHandler.getInstance().select(query);
            try {
                while (resultSet.next()) {
                    if (resultSet.getString("GENDER").equals("male"))
                    {
                        genders[0]++;
                    }
                    else
                    {
                        genders[1]++;
                    }               
                }
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserTableOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        return genders;
     }
     public int[] statusNumbersHandler(){
         //0 online 1 offline
            String query="select "+DatabaseTables.UserTable.statusFlagColumn
                    +" from "+DatabaseTables.UserTable.tableName;
            int[] status=new int[2];
            
            ResultSet resultSet=DatabaseHandler.getInstance().select(query);
            try {
                while (resultSet.next()) {
                    if (resultSet.getString("STATUSFLAG").equals("true"))
                    {
                        status[0]++;
                    }
                    else
                    {
                        status[1]++;
                    }               
                }
                resultSet.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserTableOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        return status;
     }
     public void statusHandler(long id,String status){
         String sql="UPDATE "+DatabaseTables.UserTable.tableName
                 +" SET "+DatabaseTables.UserTable.statusColumn+" = '"+status
                 +"' WHERE "+DatabaseTables.UserTable.idColumn+" = "+id;
         DatabaseHandler.getInstance().update(sql);
     }
     public void statusFlagHandler(long id,String flag){
          String sql="UPDATE "+DatabaseTables.UserTable.tableName
                 +" SET "+DatabaseTables.UserTable.statusFlagColumn+" = '"+flag
                 +"' WHERE "+DatabaseTables.UserTable.idColumn+" = "+id;
         DatabaseHandler.getInstance().update(sql);
     }
     
}
