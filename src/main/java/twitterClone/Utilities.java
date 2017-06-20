package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utilities {

	
	public void routeDisplays(boolean debug, String direction, String path){
		if (debug){
			if(direction=="in"){
				System.out.println(">>>>entering "+"/"+path+" route");				
			} else{
				System.out.println("<<<<exiting "+"/"+path+" route");				
			}
			
		}

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
	
	public void addLikes(int tweet_id, int user_id) { //updating db numLikes
		String sql = "UPDATE Tweets SET numLikes = ((SELECT numLikes FROM Tweets WHERE tweet_id = "
				+ tweet_id + ")+1) WHERE tweet_id =" + tweet_id;
		String oneLike = "select count(*) from Likers where user_id = " + user_id + " and tweet_id =" + tweet_id;
		String sql2 = "INSERT INTO Likers(tweet_id,user_id) VALUES (?,?);";
		//suggestion to use prepared statement for the above
		System.out.println(sql);
		
        try (Connection conn = insertConnect();
        		//getNumLikes
                Statement stmt  = conn.createStatement(); //used to execute UPDATE Tweets
        		
        		PreparedStatement pstmt = conn.prepareStatement(sql2); //insert Likers query
        		
        		Statement stmtOneLike  = conn.createStatement();		//select check
                ResultSet rs    = stmtOneLike.executeQuery(oneLike);) {

                
                
                if(rs.getInt("count(*)") == 0) {  
	                stmt.executeUpdate(sql); //executes UPDATE Tweet table
	                
	                pstmt.setInt(1, tweet_id); 
		            pstmt.setInt(2, user_id);
		            pstmt.executeUpdate(); //inserts row into Likers table
                }         		
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
	}
}
