package twitterClone;

import static spark.Spark.*;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public class TwitterPage {

	public static void main(String[] args) {
		staticFileLocation("public/");
		port(3000);

		boolean debug = true;

		Properties dbConnection = new Properties();
		dbConnection.put("db", "jdbc:sqlite:TwitterClone.db");
		final String dbConnection1 = dbConnection.getProperty("db");

		Utilities util_services = new Utilities();
		TwitterDAL twitterDAL = new TwitterDAL();

		if (args.length > 0) {
			String dbInitParm = args[0];
			if (args[0] == dbInitParm) {
				System.out.println("Initializing Twitter Database...");
				twitterDAL.deleteDB();
				twitterDAL.createDB();
			}
		}

		get("/register", (req, res) -> {
			util_services.routeDisplays(debug, "in", "register");
			JtwigTemplate template = JtwigTemplate
					.classpathTemplate("templates/Register.html");
			JtwigModel model = JtwigModel.newModel();
			util_services.routeDisplays(debug, "out", "register");
			return template.render(model);
		});

		get("/login", (req, res) -> {
			util_services.routeDisplays(debug, "in", "login");
			req.session().attribute("loggedin", null);
			System.out
					.println("this is your Session ID: " + req.session().id());
			JtwigTemplate template = JtwigTemplate
					.classpathTemplate("templates/Login.html");
			JtwigModel model = JtwigModel.newModel();
			util_services.routeDisplays(debug, "out", "login");
			return template.render(model);
		});

		post("/login_submit", (req, res) -> { // login check
			util_services.routeDisplays(debug, "in", "login_submit");
			String username = req.queryParams("username");
			String psw1 = req.queryParams("password1");
			User userEmpty = twitterDAL.checkLogin(username, psw1);
			String returnMessage;
			if (userEmpty != null) {
				User user = twitterDAL.selectUser(username, userEmpty);
				req.session().attribute("user", user);
				req.session().attribute("loggedin", "loggedin");
				returnMessage = "";
			} else {
				returnMessage = "Invalid username or password. Please re-enter.";
			}
			util_services.routeDisplays(debug, "out", "login_submit");
			return returnMessage;
		});

		post("/register_submit", (req, res) -> { // register
			util_services.routeDisplays(debug, "in", "register_submit");
			String username = req.queryParams("username");
			String handle = req.queryParams("handle");
			String display_name = req.queryParams("displayname");
			String psw1 = req.queryParams("password1");
			String psw2 = req.queryParams("password2");
			User user = new User(username, handle, display_name, psw1);
			String returnMessage = twitterDAL.insertUser(user, psw2);
			util_services.routeDisplays(debug, "out", "register_submit");
			return returnMessage;
		});

		get("/tweet/:username", (req, res) -> {
			util_services.routeDisplays(debug, "in", "tweet");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin == null) {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "tweet");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/tweet.html");
				JtwigModel model = JtwigModel.newModel();
				model.with("link1", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/follow\">Follow</a>");
				model.with("link4", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "tweet");
				return template.render(model);
			}
			return "";
		});

		post("/tweet1", (req, res) -> {
			util_services.routeDisplays(debug, "in", "tweet1");
			String tweetMsg = req.queryParams("tweet");
			User user = req.session().attribute("user");
			twitterDAL.insertTweet(user.getUser_id(), tweetMsg);
			util_services.routeDisplays(debug, "out", "tweet1");
			return tweetMsg;
		});

		get("/timeLine", (req, res) -> {
			util_services.routeDisplays(debug, "in", "timeLine");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin == null) {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "timeLine");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");
				String sql = "select Tweets.numLikes, Tweets.tweet_id, Tweets.user_id, "
						+ "User.user_name, User.display_name, User.handle, "
						+ "tweet_msg, date_time FROM Tweets inner join Follow on "
						+ "Tweets.user_id = Follow.target inner join User on "
						+ "Follow.target = User.user_id where Follow.user_id = "
						+ user.getUser_id()
						+ " ORDER BY date_time desc LIMIT 10;";
				System.out.println("**** SQL for user = " + sql);
				ArrayList a = twitterDAL.selectTimeline(sql);
				System.out.println(a.size());
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel();
				model.with("timeline", a);
				model.with("title", "Timeline");
				model.with("link1", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link2", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link3", "<a href=\"/follow\">Follow</a>");
				model.with("link4", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "timeLine");
				return template.render(model);
			}
			return "";
		});

		get("/user/:username", (req, res) -> {
			util_services.routeDisplays(debug, "in", "user");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin == null) {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "user");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");
				String upperGetUsername = user.getUsername().substring(0, 1)
						.toUpperCase() + user.getUsername().substring(1); // Uppercase
				String sql = "select Tweets.numLikes, Tweets.tweet_id, Tweets.user_id, User.user_name, User.display_name, User.handle, "
						+ "tweet_msg, date_time FROM Tweets inner join User on Tweets.user_id = User.user_id  "
						+ "WHERE User.user_name = \"" + user.getUsername()
						+ "\" ORDER BY date_time desc LIMIT 10;";
				System.out.println("**** SQL for user = " + sql);
				ArrayList a = twitterDAL.selectTimeline(sql);
				System.out.println(a.toString());
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel();
				model.with("timeline", a);
				model.with("title", upperGetUsername);
				model.with("link1", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/follow\">Follow</a>");
				model.with("link4", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "user");
				return template.render(model);
			}
			return "";
		});

		get("/follow", (req, res) -> {
			util_services.routeDisplays(debug, "in", "follow");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin != "loggedin") {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "follow");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");
				String sql = "select handle,display_name,User.user_name,User.user_id from User "
						+ "where User.user_id not in (select target from Follow where Follow.user_id="
						+ user.getUser_id() + ");";
				System.out.println("**** SQL for Follow = " + sql);
				ArrayList a = twitterDAL.selectFollow(sql);
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/Follow.html");
				JtwigModel model = JtwigModel.newModel().with("followlist", a);
				model.with("link1", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link4", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "follow");
				return template.render(model);
			}
			return "";
		});

		post("/follow_submit", (req, res) -> {
			util_services.routeDisplays(debug, "in", "follow_submit");
			User user = req.session().attribute("user");
			int target_id = Integer.parseInt(req.queryParams("target_id"));
			System.out.println("target id from search = " + target_id);
			twitterDAL.addFollow(user.getUser_id(), target_id);
			util_services.routeDisplays(debug, "out", "follow_submit");
			return "";
		});

		post("/like", (req, res) -> {
			int getTweetId = Integer.parseInt(req.queryParams("tweet_id"));
			User user = req.session().attribute("user");
			System.out.println(getTweetId);
			twitterDAL.addLikes(getTweetId, user.getUser_id());
			return "";
		});

		// not being used at the moment (6/22)
		post("/refreshFeed", (req, rs) -> {
			req.session().attribute("user_id");
			int user_id = req.session().attribute("user_id");
			String sql = "select Tweets.numLikes, Tweets.tweet_id, Tweets.user_id, "
					+ "User.user_name, User.display_name, User.handle, "
					+ "tweet_msg, date_time FROM Tweets inner join Follow on "
					+ "Tweets.user_id = Follow.target inner join User on "
					+ "Follow.target = User.user_id where Follow.user_id = "
					+ user_id + " ORDER BY date_time desc;";
			System.out.println("**** SQL for user = " + sql);
			ArrayList a = twitterDAL.selectTimeline(sql);
			// JtwigTemplate template =
			// JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
			//// JtwigModel model = JtwigModel.newModel();
			// model.with("timeline", a); //displays bulleted list of tweets of
			// those the user follows
			return a;
		});

	}

}