package twitterClone;

import static spark.Spark.port;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TimeLine {

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
		ArrayList<String> tweetList = new ArrayList<String>();
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			int i = 0;
			while (rs.next()) {
				tweetList.add(i, rs.getString("display_name") + rs.getString("handle") + rs.getString("tweet_msg") + rs.getString("date_time"));
				 System.out.println(
				 rs.getString("user_id") + "\t" +
				 rs.getString("display_name") + "\t" +
				 rs.getString("handle") + "\t" +
				 rs.getString("tweet_msg") + "\t" +
				 rs.getString("date_time"));
				i += 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tweetList;
	}

}
