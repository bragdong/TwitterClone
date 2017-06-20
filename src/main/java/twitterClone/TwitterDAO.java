package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TwitterDAO {

	
	private static Connection insertConnect2() {
		// SQLite connection string
		String url = "jdbc:sqlite:TwitterClone.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public  int initDB(){
		//String createUserTableSql="CREATE TABLE `User` ( `user_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_name` TEXT NOT NULL UNIQUE, `handle` TEXT NOT NULL UNIQUE, `display_name` TEXT NOT NULL, `password` TEXT NOT NULL )";
		//String createTweetTableSql="CREATE TABLE `Tweets` ( `tweet_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `tweet_msg` TEXT NOT NULL, `date_time` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, `numLikes` INTEGER DEFAULT 0, FOREIGN KEY(`user_id`) REFERENCES `User`(`user_id`) )";
		//String createFollowTableSql="CREATE TABLE `Follow` ( `follow_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `target` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES User (user_id) )";
		String sql = "CREATE TABLE if not exists `Temp` ( `follow_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `target` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES User (user_id) )";
		String sql2 = "CREATE TABLE if not exists `Temp2` ( `follow_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `target` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES User (user_id) )";
		String sql3 = "CREATE TABLE if not exists `Temp3` ( `follow_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `target` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES User (user_id) )";
		
		System.out.println("SQL for select userid = "+sql);
		int statusCode=0;
	    try (Connection conn = insertConnect2();
	            Statement stmt  = conn.createStatement();
	            										) { 
//	            ResultSet rs    = stmt.executeQuery(sql)) {  
    		stmt.execute(sql);
    		stmt.execute(sql2);
    		stmt.execute(sql3);
//	       	while(rs.next()){
//	               user_id = rs.getInt("user_id");
//	               System.out.println(user_id);
//	       	}

	       } catch (SQLException e) {
	    	   System.out.println("in catch for DBInit");
	           System.out.println(e.getMessage());
	       }
	       return statusCode;	
	}

	public  int deleteDB(){
		String sql = "DROP TABLE if exists `Temp` ";
		String sql2 = "DROP TABLE if exists `Temp2` ";
		String sql3 = "DROP TABLE if exists `Temp3` ";
		
		System.out.println("SQL for select userid = "+sql);
		int statusCode=0;
	    try (Connection conn = insertConnect2();
	            Statement stmt  = conn.createStatement();
	            										) { 
//	            ResultSet rs    = stmt.executeQuery(sql)) {  
    		stmt.execute(sql);
    		stmt.execute(sql2);
    		stmt.execute(sql3);
//	       	while(rs.next()){
//	               user_id = rs.getInt("user_id");
//	               System.out.println(user_id);
//	       	}

	       } catch (SQLException e) {
	    	   System.out.println("in catch for DBInit");
	           System.out.println(e.getMessage());
	       }
	       return statusCode;	
	}

	
	public int selectUserID(String username) {
		String sql = "SELECT user_id FROM user where user_name = \"" + username + "\"";
        System.out.println("SQL for select userid = "+sql);
        int user_id=0;
        try (Connection conn = insertConnect2();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {  
        	while(rs.next()){
                user_id = rs.getInt("user_id");
                System.out.println(user_id);
        	}
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user_id;
	}

	public String checkLogin(String username, String password) {
		String sqlUsername = "SELECT count(*) FROM User where user_name = \"" + username + "\" COLLATE NOCASE";
		String sqlUsernamePassword = "SELECT count(*) FROM User where user_name = \"" + username + "\" AND password = \"" +  password + "\"";
		String returnMessage = "";
		
        try (Connection conn = insertConnect2();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sqlUsername);
        		Statement stmt1  = conn.createStatement();
        		ResultSet rs1   = stmt1.executeQuery(sqlUsernamePassword)) {
        	
        	int getUserCount = rs.getInt("count(*)");
        	int getUserPasswordCount = rs1.getInt("count(*)");
            
        	if (getUserCount == 0) { //if username doesn't exist in database
        		returnMessage = "The username you entered doesn't exist.";
            } else if (getUserPasswordCount == 0) { //if username + password combo doesn't exist in database
        		returnMessage = "The password you entered is incorrect."; 
        		}
             else {
            	returnMessage = "";
            }
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
	
           return returnMessage;
	}
	
}
