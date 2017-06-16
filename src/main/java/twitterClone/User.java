package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	private int user_id;
	private String username;
	private String handle;
	private String display_name;
	private String password;

	private static Connection insertConnect() {
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

	
	public String insertUser(String username,String handle,String display_name,String password1,String password2){
		String sql = "INSERT INTO User(user_name,handle,display_name,password) VALUES (?,?,?,?)";
		String returnMessage = "";
		if (!password1.equals(password2)){
//			System.out.println("Passwords entered are not the same.");
			returnMessage = "Passwords entered are not the same.";
			return returnMessage;
		} else{
	        try (Connection conn = insertConnect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        	if (checkHandle(handle) == "incorrectHandleTag") {
		        	returnMessage = "Please make sure your handle begins with '@'.";
		        } else if (checkUser(username) == "pass" && checkHandle(handle) == "pass") {
		        	pstmt.setString(1, username);
		            pstmt.setString(2, handle);
		            pstmt.setString(3, display_name);
		            pstmt.setString(4, password1);
		            pstmt.executeUpdate();
		            returnMessage = "";}
	            else if (checkUser(username) == "pass" && checkHandle(handle) == "dupHandle"){
	            	System.out.println("That handle is already in use. Please choose another.");
	            	returnMessage = "That handle is already in use. Please choose another.";
		        } else if (checkUser(username) == "dupUsername" && checkHandle(handle) == "pass") {
		        	System.out.println("That username is already in use. Please choose another.");
		        	returnMessage = "That username is already in use. Please choose another.";
		        } else {
		        	System.out.println("The username and handle you've selected are already in use.");
		        	returnMessage = "The username and handle you've selected are already in use.";
		        }
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return returnMessage;
		}
	}
	
	public void addFollow(int user_id,int target){
		String sql = "INSERT INTO Follow(user_id,target) VALUES (?,?)";
        try (Connection conn = insertConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        	pstmt.setInt(1, user_id);
	            pstmt.setInt(2, target);
	            pstmt.executeUpdate();          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public int selectUserID(String username) {
		String sql = "SELECT user_id FROM user where user_name = \"" + username + "\"";
        System.out.println("SQL for select userid = "+sql);
        int user_id=0;
        try (Connection conn = insertConnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {  
        	System.out.println("before while");
        	while(rs.next()){
        		System.out.println("in while loop");
                user_id = rs.getInt("user_id");
                System.out.println(user_id);
        	}
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user_id;
	}
	
	public String checkUser(String username) {
		String sqlUsername = "SELECT count(*) FROM User where user_name = \"" + username + "\"";
        
        try (Connection conn = insertConnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlUsername)) {
            
            if (rs.getInt("count(*)") >= 1) {
            	return "dupUsername";
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "pass";
	}
	
	public String checkHandle(String handle) {
		String sqlHandle = "SELECT count(*) FROM User where handle = \"" + handle + "\"";
		System.out.println(sqlHandle);
		
        try (Connection conn = insertConnect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sqlHandle)) {
               
        	if (!handle.startsWith("@")) {
         	   return "incorrectHandleTag";
            } else if (rs.getInt("count(*)") >= 1) {
               	return "dupHandle";
               }
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
	
           return "pass";
	}
	
	public String checkLogin(String username, String password) {
		String sqlUsername = "SELECT count(*) FROM User where user_name = \"" + username + "\"";
		String sqlUsernamePassword = "SELECT count(*) FROM User where user_name = \"" + username + "\" AND password = \"" +  password + "\"";
		String returnMessage = "";
		
        try (Connection conn = insertConnect();
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
