/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author Asmaa
 */
public class FriendTableOperation {
    public ArrayList<User> friendListHandler(long id){
        ArrayList<User> usersList = new ArrayList<>();
        String sql = "SELECT * from "+DatabaseTables.UserTable.tableName      
                    +" where "+DatabaseTables.UserTable.idColumn
                    +" in ((select "+DatabaseTables.FriendsTable.friendIDColumn
                    +" from "+DatabaseTables.FriendsTable.tableName
                    +" where "+DatabaseTables.FriendsTable.tableName+"."
                    +DatabaseTables.FriendsTable.userIDColumn+" = "+id+" and "
                    +DatabaseTables.FriendsTable.tableName+"."
                    +DatabaseTables.FriendsTable.requestFlagColumn+" != 0) UNION (select "
                    +DatabaseTables.FriendsTable.userIDColumn+" from "
                    +DatabaseTables.FriendsTable.tableName
                    +" where "+DatabaseTables.FriendsTable.tableName+"."
                    +DatabaseTables.FriendsTable.friendIDColumn+" = "+id+" and "
                    +DatabaseTables.FriendsTable.tableName+"."
                    +DatabaseTables.FriendsTable.requestFlagColumn+" != 0))";

        ResultSet resultSet= DatabaseHandler.getInstance().select(sql);
        try {
            while (resultSet.next()) {
                usersList.add(
                        new User(resultSet.getLong(DatabaseTables.UserTable.idColumn),
                                resultSet.getString(DatabaseTables.UserTable.nameColumn),
                                resultSet.getString(DatabaseTables.UserTable.emailColumn),
                                resultSet.getString(DatabaseTables.UserTable.passwordColumn),
                                resultSet.getString(DatabaseTables.UserTable.genderColumn),
                                resultSet.getString(DatabaseTables.UserTable.statusColumn),
                                resultSet.getString(DatabaseTables.UserTable.statusFlagColumn)
                        )
                );
            }
            resultSet.close();
         } catch (SQLException ex) {
            Logger.getLogger(FriendTableOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return usersList;
    }
 
    public boolean addFriendHandler(long id,String friendEmail){
        boolean result=false;
        try {
            String checkUserQuery="SELECT id from users where lower(email) = lower('"+friendEmail+"')";
            ResultSet resultSet=DatabaseHandler.getInstance().select(checkUserQuery);
            if(resultSet.next()){
                long friendID=resultSet.getLong(DatabaseTables.UserTable.idColumn);
                String checkFriendQuery = "select "+DatabaseTables.FriendsTable.friendIDColumn
                         +" from "+DatabaseTables.FriendsTable.tableName
                         +" where (addfriend.USERID = "+id+" and ( addfriend.REQUESTFLAG != 0 or REQUESTFLAG = 0 ) and addfriend.FRIENDID = " + friendID + ")";
                resultSet=DatabaseHandler.getInstance().select(checkFriendQuery);
                if(resultSet.next()){
                    result=false;
                }
                else{
                    String query="INSERT INTO "+DatabaseTables.FriendsTable.tableName+" values ("+id+","+friendID+","+"0)";
                    DatabaseHandler.getInstance().insert(query);
                    result=true;
                }
     
            }
            
            resultSet.close();  
            
        } catch (SQLException ex) {
            Logger.getLogger(FriendTableOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }
    public ArrayList<User> requestListHandler(long id){
        ArrayList<User> requestsList = new ArrayList<>();
        String sql = "select * from "+DatabaseTables.UserTable.tableName
                   +" where "+DatabaseTables.UserTable.idColumn
                   +" in ( select "+DatabaseTables.FriendsTable.userIDColumn
                   +" from "+DatabaseTables.FriendsTable.tableName
                   +" where "+DatabaseTables.FriendsTable.friendIDColumn
                   +" = "+id+" and "+DatabaseTables.FriendsTable.requestFlagColumn+" = 0)";

        ResultSet resultSet= DatabaseHandler.getInstance().select(sql);
        try {
            while (resultSet.next()) {
                requestsList.add(
                        new User(resultSet.getLong(DatabaseTables.UserTable.idColumn),
                                resultSet.getString(DatabaseTables.UserTable.nameColumn),
                                resultSet.getString(DatabaseTables.UserTable.emailColumn),
                                resultSet.getString(DatabaseTables.UserTable.passwordColumn),
                                resultSet.getString(DatabaseTables.UserTable.genderColumn),
                                resultSet.getString(DatabaseTables.UserTable.statusColumn),
                                resultSet.getString(DatabaseTables.UserTable.statusFlagColumn)
                        )
                );
            }
            resultSet.close();
         } catch (SQLException ex) {
            Logger.getLogger(FriendTableOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return requestsList;
    }
    public void acceptRequestHandler(long userID,long friendID){
        String query="UPDATE "+DatabaseTables.FriendsTable.tableName
                     +" SET "+DatabaseTables.FriendsTable.requestFlagColumn+" = 1"
                     +" WHERE "+DatabaseTables.FriendsTable.userIDColumn+" = "+friendID
                     +" AND "+DatabaseTables.FriendsTable.friendIDColumn+" = "+userID;
        DatabaseHandler.getInstance().update(query);
    }
    public void rejectRequestHandler(long userID,long friendID){
        String query="Delete FROM "+DatabaseTables.FriendsTable.tableName
                     +" where "+DatabaseTables.FriendsTable.userIDColumn+" = "+friendID
                     +" and "+DatabaseTables.FriendsTable.friendIDColumn+" = "+userID;
        DatabaseHandler.getInstance().delete(query);
    }
    
}
