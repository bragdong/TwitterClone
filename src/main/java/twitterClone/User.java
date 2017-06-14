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

	
	public void insertUser(String username,String handle,String display_name,String password1,String password2){
		String sql = "INSERT INTO User(user_name,handle,display_name,password) VALUES (?,?,?,?)";
		if (!password1.equals(password2)){
//			System.out.println("Passwords entered are not the same.");
		} else{
					
	        try (Connection conn = insertConnect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            if (checkUser(username) == "pass" && checkHandle(handle) == "pass") {
		        	pstmt.setString(1, username);
		            pstmt.setString(2, handle);
		            pstmt.setString(3, display_name);
		            pstmt.setString(4, password1);
		            pstmt.executeUpdate(); }
	            else if (checkUser(username) == "pass" && checkHandle(handle) == "dupHandle"){
	            	System.out.println("That handle is already in use. Please choose another.");
		        } else if (checkUser(username) == "dupUsername" && checkHandle(handle) == "pass") {
		        	System.out.println("That username is already in use. Please choose another.");
		        } else {
		        	System.out.println("The username and handle you've selected are already in use.");
		        }
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		}
	}
	
	
	public String checkUser(String username) {
		String sqlUsername = "SELECT count(*) FROM User where user_name = \"" + username + "\"";
        
        try (Connection conn = insertConnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlUsername)){
            
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
                ResultSet rs    = stmt.executeQuery(sqlHandle)){
               
               if (rs.getInt("count(*)") >= 1) {
               	return "dupHandle";
               }
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
           return "pass";
   	
	}
}
