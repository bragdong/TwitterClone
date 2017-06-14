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
		String url = "jdbc:sqlite:/TwitterClone.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	
	public  void insertUser(String username,String handle,String display_name,String password1,String password2){
		String sql = "INSERT INTO User(user_name,handle,display_name,password) VALUES (?,?,?,?)";
		if (!password1.equals(password2)){
			System.out.println("Passwords entered are not the same.");
		} else{
					
	        try (Connection conn = insertConnect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, handle);
	            pstmt.setString(3, display_name);
	            pstmt.setString(4, password1);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }	
		}
	}
}
