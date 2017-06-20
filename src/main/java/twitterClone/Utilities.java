package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	
	public void addLikes(int tweet_id) {
		String sql = "UPDATE Tweets SET numLikes = ((SELECT numLikes FROM Tweets WHERE tweet_id = "
				+ tweet_id + ")+1) WHERE tweet_id =" + tweet_id;
		System.out.println(sql);
		
        try (Connection conn = insertConnect();
        		//getNumLikes
                Statement stmt  = conn.createStatement();) {

                stmt.executeUpdate(sql);
        		
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
	}
}
