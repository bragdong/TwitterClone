package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tweet {

	private String tweet_msg;
	private String create_dtetime;
	private int user_id;
	
	public String getTweet_msg() {
		return tweet_msg;
	}
	public void setTweet_msg(String tweet_msg) {
		this.tweet_msg = tweet_msg;
	}
	public String getCreate_dtetime() {
		return create_dtetime;
	}
	public void setCreate_dtetime(String create_dtetime) {
		this.create_dtetime = create_dtetime;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
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
	
	public void insertTweet(int user_id, String tweet) {
		String sql = "INSERT INTO Tweets(user_id,tweet_msg) VALUES (?,?)";
		
	        try (Connection conn = insertConnect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        	pstmt.setInt(1, user_id);
		            pstmt.setString(2, tweet);
		            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		}
	}
