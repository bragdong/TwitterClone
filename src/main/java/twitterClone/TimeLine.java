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
			while (rs.next()) {
				int likes = 0;
				if (rs.getString("numLikes") != null) {
					likes = rs.getInt("numLikes");
				}
				tweetList.add("<a href=\"/user/" + rs.getString("user_name") + "\">" + rs.getString("display_name")
						+ "</a>" + "&nbsp" + "<a href=\"/user/" + rs.getString("user_name") + "\">"
						+ rs.getString("handle") + "</a>" + "&nbsp" + rs.getString("date_time") + "<br>"
						+ rs.getString("tweet_msg") + "<br>" + "<button id=\"" + rs.getInt("tweet_id")
						+ "\" type=\"button\" onclick=\"myFunction(this.id)\">" + "Like (" + likes + ")</button>");
				System.out.println(rs.getString("user_id") + "\t" + rs.getString("user_name") + "\t"
						+ rs.getString("display_name") + "\t" + rs.getString("handle") + "\t"
						+ rs.getString("tweet_msg") + "\t" + rs.getString("date_time"));
				// use printf for the above?
			}
			if (tweetList.size() == 0) {
				tweetList.add("There aren't any tweets to display.  Click the \"Tweet\" link above "
						+ "to create a new tweet, or click the \"Follow\" link to choose other users to follow!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tweetList;
	}

}
