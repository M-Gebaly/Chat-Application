/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Asmaa
 */
public class DatabaseTables {
   static class UserTable{
        public static String tableName="users";
        public static String idColumn="id";
        public static String nameColumn="name";
        public static String emailColumn="email";
        public static String passwordColumn="password";
        public static String genderColumn="gender";
        public static String statusColumn="status";
        public static String statusFlagColumn="statusFlag";
       
    }
   static class FriendsTable{
       public static String tableName="addFriend";
       public static String userIDColumn="userID";
       public static String friendIDColumn="friendID";
       public static String requestFlagColumn="requestFlag";

   }
    
}
