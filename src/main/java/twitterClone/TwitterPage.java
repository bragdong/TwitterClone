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

		boolean debug = true; // display entry and exits to various routes to
								// standard output

		Properties dbConnection = new Properties();
		dbConnection.put("db", "jdbc:sqlite:TwitterClone.db");
		final String dbConnection1 = dbConnection.getProperty("db");

		Utilities util_services = new Utilities();
		TwitterDAL twitterDAL = new TwitterDAL();
		util_services.dbInitCheck(twitterDAL, args);

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
				model.with("link3", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link4", "<a href=\"/follow\">Follow</a>");
				model.with("link5", "<a href=\"/login\">Log out</a>");
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

		get("/timeLine", (req, res) -> { // timeline based on users you're
											// following
			util_services.routeDisplays(debug, "in", "timeLine");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin == null) {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "timeLine");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");

				ArrayList a = twitterDAL.selectTimeline("follows", user, "");
				System.out.println(a.size());
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel();
				model.with("timeline", a);
				model.with("title", "Timeline");
				model.with("link1", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link4", "<a href=\"/follow\">Follow</a>");
				model.with("link5", "<a href=\"/login\">Log out</a>");
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
				String getUsername = req.params(":username");
				String upperGetUsername = getUsername.substring(0, 1)
						.toUpperCase() + getUsername.substring(1);
				ArrayList a = twitterDAL.selectTimeline("self", user,
						getUsername);
				System.out.println(a.toString());
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel();
				model.with("timeline", a);
				model.with("title", upperGetUsername);
				model.with("link1", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link4", "<a href=\"/follow\">Follow</a>");
				model.with("link5", "<a href=\"/login\">Log out</a>");
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

				ArrayList a = twitterDAL.selectFollow(user);
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/Follow.html");
				JtwigModel model = JtwigModel.newModel().with("followlist", a);
				model.with("link1", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link4", "<a href=\"/follow\">Follow</a>");
				model.with("link5", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "follow");
				return template.render(model);
			}
			return "";
		});

		get("/unfollow", (req, res) -> {
			util_services.routeDisplays(debug, "in", "unfollow");
			String loggedin = req.session().attribute("loggedin");
			if (loggedin != "loggedin") {
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug, "out", "unfollow");
				res.redirect(redirectUrl);
			} else {
				User user = req.session().attribute("user");

				ArrayList a = twitterDAL.selectUnFollow(user);
				JtwigTemplate template = JtwigTemplate
						.classpathTemplate("templates/UnFollow.html");
				JtwigModel model = JtwigModel.newModel().with("followlist", a);
				model.with("link1", "<a href=\"/tweet/" + user.getUsername()
						+ "\">Tweet</a>");
				model.with("link2", "<a href=\"/timeLine\">Timeline</a>");
				model.with("link3", "<a href=\"/user/" + user.getUsername()
						+ "\">Your Profile</a>");
				model.with("link4", "<a href=\"/login\">Log out</a>");
				util_services.routeDisplays(debug, "out", "unfollow");
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
		
		post("/unfollow_submit", (req, res) -> {
			util_services.routeDisplays(debug, "in", "unfollow_submit");
			User user = req.session().attribute("user");
			int target_id = Integer.parseInt(req.queryParams("target_id"));
			System.out.println("target id from search = " + target_id);
			twitterDAL.unFollow(user,target_id);
			util_services.routeDisplays(debug, "out", "unfollow_submit");
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
		// post("/refreshFeed", (req, rs) -> {
		// req.session().attribute("user_id");
		// int user_id = req.session().attribute("user_id");
		// String sql = "select Tweets.numLikes, Tweets.tweet_id,
		// Tweets.user_id, "
		// + "User.user_name, User.display_name, User.handle, "
		// + "tweet_msg, date_time FROM Tweets inner join Follow on "
		// + "Tweets.user_id = Follow.target inner join User on "
		// + "Follow.target = User.user_id where Follow.user_id = "
		// + user_id + " ORDER BY date_time desc;";
		// System.out.println("**** SQL for user = " + sql);
		// ArrayList a = twitterDAL.selectTimeline(sql);
		// JtwigTemplate template =
		// JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
		//// JtwigModel model = JtwigModel.newModel();
		// model.with("timeline", a); //displays bulleted list of tweets of
		// those the user follows
		// return a;
		// });

	}

}