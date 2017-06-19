package twitterClone;

import static spark.Spark.port;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Follow {

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

	public ArrayList selectTimeline(String sql) {
		ArrayList<String> followlist = new ArrayList<String>();
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			int i = 0;
			while (rs.next()) {
//				                                                                                                                        <button type="button">Click Me!</button>
				followlist.add(i, rs.getString("display_name") + rs.getString("handle") + "&nbsp" + rs.getString("user_id") + "&nbsp" + "<button type=\"button\"> Follow</button");
//				followlist.add(i, "<a href=\"/user/" + rs.getString("user_name") + "\">" + rs.getString("display_name") + "</a>" + "&nbsp" + "<a href=\"/user/" + rs.getString("user_name") + "\">" + rs.getString("handle") + "</a>" + "&nbsp" +  rs.getString("user_id"));
//				tweetList.add(i, "<a href=\"/user/" + rs.getString("user_name") + "\">" + rs.getString("display_name") + "</a>" + "&nbsp" + "<a href=\"/user/" + rs.getString("user_name") + "\">" + rs.getString("handle") + "</a>" + "&nbsp" + rs.getString("date_time") + "<br>" + rs.getString("tweet_msg") );
				System.out.println(
				 rs.getString("handle") + "\t" +
				 rs.getString("user_name") + "\t" +
				 rs.getString("user_id") + "\t" +
				 rs.getString("display_name"));
				i += 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return followlist;
	}

	public void addFollow(int user_id,int target){  //MADE A COPY OF THIS FROM USER CLASS. SHOULD WE DELETE USER ONE??
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
	
}
