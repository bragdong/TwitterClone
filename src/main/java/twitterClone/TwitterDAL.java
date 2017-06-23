package twitterClone;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TwitterDAL {

	private static Connection insertConnect() {
		// SQLite connection string
		final String url = "jdbc:sqlite:TwitterClone.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public int createDB() {
		String createUserTableSql = "CREATE TABLE if not exists `User` ( `user_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_name` TEXT NOT NULL UNIQUE, `handle` TEXT NOT NULL UNIQUE, `display_name` TEXT NOT NULL, `password` TEXT NOT NULL )";
		String createTweetTableSql = "CREATE TABLE if not exists `Tweets` ( `tweet_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `tweet_msg` TEXT NOT NULL, `date_time` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, `numLikes` INTEGER DEFAULT 0, FOREIGN KEY(`user_id`) REFERENCES `User`(`user_id`) )";
		String createFollowTableSql = "CREATE TABLE if not exists `Follow` ( `follow_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `user_id` INTEGER NOT NULL, `target` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES User (user_id) )";
		String createLikersTableSql = "CREATE TABLE if not exists `Likers` ( `like_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `tweet_id` INTEGER, `user_id` INTEGER, FOREIGN KEY(`tweet_id`) REFERENCES Tweets(tweet_id), FOREIGN KEY(`user_id`) REFERENCES User(user_id) )";

		System.out.println("Creating... " + createUserTableSql);
		System.out.println("Creating... " + createTweetTableSql);
		System.out.println("Creating... " + createFollowTableSql);
		System.out.println("Creating... " + createLikersTableSql);
		System.out.println("Twitter Table creation complete.");
		int statusCode = 0;
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();) {
			stmt.execute(createUserTableSql);
			stmt.execute(createTweetTableSql);
			stmt.execute(createFollowTableSql);
			stmt.execute(createLikersTableSql);
		} catch (SQLException e) {
			System.out.println("in catch for DBInit");
			System.out.println(e.getMessage());
		}
		return statusCode;
	}

	public int deleteDB() {
		String dropUserTableSql = "DROP TABLE if exists `User` ";
		String dropTweetTableSql = "DROP TABLE if exists `Tweets` ";
		String dropFollowTableSql = "DROP TABLE if exists `Follow` ";
		String dropLikersTableSql = "DROP TABLE if exists `Likers` ";

		System.out.println("Dropping table... " + dropUserTableSql);
		System.out.println("Dropping table... " + dropTweetTableSql);
		System.out.println("Dropping table... " + dropFollowTableSql);
		System.out.println("Dropping table... " + dropLikersTableSql);
		System.out.println("Drop tables complete.");
		int statusCode = 0;
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();) {
			stmt.execute(dropUserTableSql);
			stmt.execute(dropTweetTableSql);
			stmt.execute(dropFollowTableSql);
			stmt.execute(dropLikersTableSql);
		} catch (SQLException e) {
			System.out.println("in catch for DBInit");
			System.out.println(e.getMessage());
		}
		return statusCode;
	}

	public User selectUser(String username, User user) {
		String sql = "SELECT user_id, handle, display_name FROM user where user_name = \""
				+ username + "\"";
		System.out.println("SQL for select userid = " + sql);
		int user_id = 0;
		String handle;
		String display_name;

		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				user_id = rs.getInt("user_id");
				handle = rs.getString("handle");
				display_name = rs.getString("display_name");

				user.setUser_id(user_id);
				user.setUsername(username);
				user.setHandle(handle);
				user.setDisplay_name(display_name);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return user;
	}


public User checkLogin(String username, String password)
		throws SQLException {
	String hashedPassword = hashPassword(password);
	String sqlUsernamePassword = "SELECT count(*) FROM User where user_name = \""
			+ username + "\" AND password = \"" + hashedPassword + "\"";

		User user; 

		try (Connection conn = insertConnect();
				Statement stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(sqlUsernamePassword)) {

			int getUserPasswordCount = rs1.getInt("count(*)");

			if (getUserPasswordCount == 0) {
				return null;
			} else {
				user = new User("", "", "", "");
			}
		}

		return user;
	}

	public String hashPassword(String password) {
		final String SALT = "GOFORCODE";
		String hashedPassword = password + SALT;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException ex) {
			System.out.println(ex);
		}
		md.update(password.getBytes());
		String digest = new String(md.digest());
		hashedPassword = digest;

		return (String) hashedPassword;
	}

	public ArrayList<String> selectTimeline(String timeLineQueryParm, User user, String getUsername) {
		ArrayList<String> tweetList = new ArrayList<String>();
		String sql = "";
		if (timeLineQueryParm.equals("follows")){
			 sql = "select Tweets.numLikes, Tweets.tweet_id, Tweets.user_id, "
					+ "User.user_name, User.display_name, User.handle, "
					+ "tweet_msg, date_time FROM Tweets inner join Follow on "
					+ "Tweets.user_id = Follow.target inner join User on "
					+ "Follow.target = User.user_id where Follow.user_id = "
					+ user.getUser_id()
					+ " ORDER BY date_time desc LIMIT 20;";
			System.out.println("**** SQL for select follow timeline = " + sql);
		} else if (timeLineQueryParm.equals("self")){
			 sql = "select Tweets.numLikes, Tweets.tweet_id, Tweets.user_id, User.user_name, User.display_name, User.handle, "
					+ "tweet_msg, date_time FROM Tweets inner join User on Tweets.user_id = User.user_id  "
					+ "WHERE User.user_name = \"" + getUsername
					+ "\" ORDER BY date_time desc LIMIT 20;";
			System.out.println("**** SQL for select self timeline  = " + sql);
		} else {
			System.out.println("Error in passing parm into selectTimeline method.");
		}
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				int likes = rs.getInt("numLikes");
				tweetList.add("<a href=\"/user/" + rs.getString("user_name")
						+ "\">" + rs.getString("display_name") + "</a>"
						+ "&nbsp" + "<a href=\"/user/"
						+ rs.getString("user_name") + "\">"
						+ rs.getString("handle") + "</a>" + "&nbsp"
						+ rs.getString("date_time") + "<br>"
						+ rs.getString("tweet_msg") + "<br>" + "<button id=\""
						+ rs.getInt("tweet_id")
						+ "\" type=\"button\" onclick=\"myFunction(this.id)\">"
						+ "Like (" + likes + ")</button>");
			}
			if (tweetList.size() == 0) {
				tweetList
						.add("There aren't any tweets to display.  Click the \"Tweet\" link above "
								+ "to create a new tweet, or click the \"Follow\" link to choose other users to follow!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tweetList;
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

	public void addLikes(int tweet_id, int user_id) { 
		String updateNumLikes = "UPDATE Tweets SET numLikes = ((SELECT numLikes FROM Tweets WHERE tweet_id = ?) + 1)"
				+ "WHERE tweet_id = ?";
		String getNumLikes = "select count(*) from Likers where user_id = "
				+ user_id + " and tweet_id =" + tweet_id;
		String insertLikers = "INSERT INTO Likers(tweet_id,user_id) VALUES (?,?);";

		try (Connection conn = insertConnect();
				PreparedStatement updatePstmt = conn.prepareStatement(updateNumLikes);

				PreparedStatement insertPstmt = conn.prepareStatement(insertLikers);

				Statement stmtOneLike = conn.createStatement();
				ResultSet rs = stmtOneLike.executeQuery(getNumLikes);) {

			if (rs.getInt("count(*)") == 0) {
				updatePstmt.setInt(1, tweet_id);
				updatePstmt.setInt(2, tweet_id);
				System.out.println(updatePstmt);
				updatePstmt.executeUpdate();
				
				insertPstmt.setInt(1, tweet_id);
				insertPstmt.setInt(2, user_id);
				insertPstmt.executeUpdate(); 
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<String> selectFollow(User user) {
		String sql = "select handle,display_name,User.user_name,User.user_id from User "
				+ "where User.user_id not in (select target from Follow where Follow.user_id="
				+ user.getUser_id() + ");";
		System.out.println("**** SQL for Follow = " + sql);
		ArrayList<String> followlist = new ArrayList<String>();
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			int i = 0;
			while (rs.next()) {
				followlist.add(i, rs.getString("display_name") + "&emsp;"
						+ rs.getString("handle") + "&emsp;" + "<button id=\""
						+ rs.getString("user_id")
						+ "\" type=\"button\" onclick=\"myFunction(this.id)\"> Follow</button>");
				i += 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return followlist;
	}

	public ArrayList<String> selectUnFollow(User user) {
		String sql = "select handle,display_name,User.user_name,User.user_id from User "
				+ "where User.user_id in (select target from Follow where Follow.user_id="
				+ user.getUser_id() + " and Follow.target not in ( "+ user.getUser_id() +") );";
		System.out.println("**** SQL for UnFollow = " + sql);
		ArrayList<String> followlist = new ArrayList<String>();
		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			int i = 0;
			while (rs.next()) {
				followlist.add(i, rs.getString("display_name") + "&emsp;"
						+ rs.getString("handle") + "&emsp;" + "<button id=\""
						+ rs.getString("user_id")
						+ "\" type=\"button\" onclick=\"myFunction(this.id)\"> UnFollow</button>");
				i += 1;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return followlist;
	}	
	
	public void addFollow(int user_id, int target) {
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

	public void unFollow(User user, int target) {
		String sql = "DELETE FROM Follow WHERE user_id = ? and target = ?";
		System.out.println("delete sql = "+sql);
		try (Connection conn = insertConnect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, user.getUser_id());
			pstmt.setInt(2, target);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String insertUser(User user, String password2) throws SQLException {
		String sql = "INSERT INTO User(user_name,handle,display_name,password) VALUES (?,?,?,?)";
		if (!user.getPassword1().equals(password2)) {
			return "Passwords entered are not the same.";
		} else if (checkHandle(user.getHandle()) == "incorrectHandleTag") {
			return "Please make sure your handle begins with '@'.";
		} else if (checkUser(user.getUsername()) == "pass"
				&& checkHandle(user.getHandle()) == "dupHandle") {
			System.out.println(
					"That handle is already in use. Please choose another.");
			return "That handle is already in use. Please choose another.";
		} else if (checkUser(user.getUsername()) == "dupUsername"
				&& checkHandle(user.getHandle()) == "pass") {
			System.out.println(
					"That username is already in use. Please choose another.");
			return "That username is already in use. Please choose another.";
		} else if (checkUser(user.getUsername()) == "dupUsername"
				&& checkHandle(user.getHandle()) == "dupHandle") {
			System.out.println(
					"The username and handle you've selected are already in use.");
			return "The username and handle you've selected are already in use.";
		}
		try (Connection conn = insertConnect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			try {
				conn.setAutoCommit(false);
				if (checkUser(user.getUsername()) == "pass"
						&& checkHandle(user.getHandle()) == "pass") {

					// HASH PASSWORD
					TwitterDAL twitterDAL = new TwitterDAL();
					String hashedPassword = twitterDAL
							.hashPassword(user.getPassword1());

					pstmt.setString(1, user.getUsername());
					pstmt.setString(2, user.getHandle());
					pstmt.setString(3, user.getDisplay_name());
					pstmt.setString(4, hashedPassword);
					pstmt.executeUpdate();
					String sql1 = "SELECT user_id FROM user where user_name = \""
							+ user.getUsername() + "\"";
					System.out.println("SQL for select userid = " + sql1);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql1);
					while (rs.next()) {
						int user_id = rs.getInt("user_id");
						user.setUser_id(user_id);
					}
					// User follows self
					String sql2 = "INSERT INTO Follow(user_id,target) VALUES (?,?)";
					PreparedStatement pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setInt(1, user.getUser_id());
					pstmt2.setInt(2, user.getUser_id());
					pstmt2.executeUpdate();
					System.out.println("before commit");
					conn.commit();
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
		return "";

	}

	public String checkUser(String username) {
		String sqlUsername = "SELECT count(*) FROM User where user_name = \""
				+ username + "\" COLLATE NOCASE";

		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlUsername)) {

			if (rs.getInt("count(*)") >= 1) {
				return "dupUsername";
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return "pass";
	}

	public String checkHandle(String handle) {
		String sqlHandle = "SELECT count(*) FROM User where handle = \""
				+ handle + "\" COLLATE NOCASE";
		System.out.println(sqlHandle);

		try (Connection conn = insertConnect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlHandle)) {

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

}
