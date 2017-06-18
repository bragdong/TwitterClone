package twitterClone;

import static spark.Spark.*;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.FileOutputStream;
import java.util.ArrayList;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public class TwitterPage {

	public static void main(String[] args) {
		staticFileLocation("public/");
		port(3000);
		boolean debug = true;
		Utilities util_services = new Utilities();
		TimeLine timeline = new TimeLine();
		Follow follow = new Follow();

		get("/register", (req, res) -> {
			util_services.routeDisplays(debug,"in","register");
			System.out.println("entering /register route...");
			JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/Register.html");
			JtwigModel model = JtwigModel.newModel();
			util_services.routeDisplays(debug,"out","register");
			return template.render(model);
		});

		get("/login", (req, res) -> {
			util_services.routeDisplays(debug,"in","login");
			System.out.println("this is your Session ID: " + req.session().id());
			JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/Login.html");
			JtwigModel model = JtwigModel.newModel();
			util_services.routeDisplays(debug,"out","login");
			return template.render(model);
		});

		post("/login_submit", (req, res) -> {							//login check
			util_services.routeDisplays(debug,"in","login_submit");
//			System.out.println("Is session new? "+ req.session().isNew());  //why is it false?
			String username = req.queryParams("username");
			String psw1 = req.queryParams("password1");
//			String handle = req.queryParams("handle");
//			String displayName = req.queryParams(displayName);
			User user = new User();
			String returnMessage = user.checkLogin(username, psw1);
			if (returnMessage == ""){
				System.out.println("user found so update session object properties.");
				int user_id = user.selectUserID(username);
				req.session().attribute("user_id",user_id);
				req.session().attribute("username", username);
				req.session().attribute("loggedin", "loggedin");
//				req.session().attribute("handle", handle);
//				req.session().attribute("displayName", "loggedin");
//				String s = req.session().attribute("username");
//				boolean l = req.session().attribute("loggedin");
				System.out.println("User logged in = " + req.session().attribute("username"));
				System.out.println("User ID = " + req.session().attribute("user_id"));
				System.out.println("logged in status = " + req.session().attribute("loggedin"));				
			}

			// System.out.println("Username entered = "+username);
			// System.out.println("Password entered = "+psw1);
			// System.out.println("Call checkUser and navigate to user Home Page
			// if valid.");
			util_services.routeDisplays(debug,"out","login_submit");			
			return returnMessage;
		});

		post("/register_submit", (req, res) -> { 								//register
			util_services.routeDisplays(debug,"in","register_submit");
			String username = req.queryParams("username");
			String handle = req.queryParams("handle");
			String display_name = req.queryParams("displayname");
			String psw1 = req.queryParams("password1");
			String psw2 = req.queryParams("password2");
			User user = new User();
			// int returnCde = 0;
			// returnCde=user.insertUser(username, handle, display_name, psw1,
			// psw2);
			String returnMessage = user.insertUser(username, handle, display_name, psw1, psw2);
			if (returnMessage == ""){
				int user_id = user.selectUserID(username);
				req.session().attribute("user_id",user_id);
				user.addFollow(user_id, user_id);
			}

			// if(returnCde == -1){
			// System.out.println("Passwords entered are not the same.");
			// System.out.println("show register page again");
			// } else{
			// System.out.println("back in main page");
			// System.out.println("navigate to user home page");
			// }

			// String second = req.queryParams("second");
			// try {
			// int a = Integer.parseInt(first);
			//// int b = Integer.parseInt(second);
			// return new Integer(a + b).toString();
			// } catch (NumberFormatException ex) {
			// System.out.println("bad input");
			// }
			// return "failure";
			util_services.routeDisplays(debug,"out","register_submit");
			return returnMessage;
		});

		get("/tweet", (req, res) -> {
			util_services.routeDisplays(debug,"in","tweet");
			String loggedin = req.session().attribute("loggedin");
			if(loggedin == null){
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug,"out","tweet");
				res.redirect(redirectUrl);	
			} else{
				JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/tweet.html");
				JtwigModel model = JtwigModel.newModel();
				util_services.routeDisplays(debug,"out","tweet");
				return template.render(model);
			}
			return "";
		});

		post("/tweet1", (req, res) -> {
			util_services.routeDisplays(debug,"in","tweet1");
			String tweetMsg = req.queryParams("tweet");
			Tweet tweet = new Tweet();
			System.out.println("User logged in = " + req.session().attribute("username"));
			System.out.println("User ID = " + req.session().attribute("user_id"));
			System.out.println("logged in status = " + req.session().attribute("loggedin"));
			int user_id=req.session().attribute("user_id");
			System.out.println("id before insert"+user_id);
			tweet.insertTweet(user_id, tweetMsg);
			util_services.routeDisplays(debug,"out","tweet1");
			return tweetMsg;
		});

		get("/timeLine", (req, res) -> {
			util_services.routeDisplays(debug,"in","timeLine");
			User user = new User();
//			req.session().attribute("username");
			req.session().attribute("user_id");
			int user_id = req.session().attribute("user_id");
			System.out.println(user_id);
//			int user_id = user.selectUserID(username);
			String loggedin = req.session().attribute("loggedin");
			if(loggedin == null){
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug,"out","timeLine");
				res.redirect(redirectUrl);	
			} else {
				String sql = "select Tweets.user_id, User.display_name, User.handle, tweet_msg, date_time FROM Tweets inner join Follow on Tweets.user_id = Follow.target inner join User on Follow.target = User.user_id where Follow.user_id = " + user_id + " ORDER BY date_time desc;";
				System.out.println("**** SQL for user = "+sql);
				ArrayList a = timeline.selectTimeline(sql);
				JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel().with("timeline", a);
				// Timeline.getTimeline(user_id);
				util_services.routeDisplays(debug,"out","timeLine");
				return template.render(model);
			}
			return "";
		});

		get("/user", (req, res) -> {
			util_services.routeDisplays(debug,"in","user");
			String loggedin = req.session().attribute("loggedin");
			if(loggedin == null){
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug,"out","user");
				res.redirect(redirectUrl);				
			} else {
				int user_id=req.session().attribute("user_id");			
				String sql = "select Tweets.user_id, User.display_name, User.handle, tweet_msg, date_time FROM Tweets inner join User on Tweets.user_id = User.user_id  WHERE user.user_id = " + user_id + " ORDER BY date_time desc;";
				System.out.println("**** SQL for user = "+sql);
				ArrayList a = timeline.selectTimeline(sql);
				JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
				JtwigModel model = JtwigModel.newModel().with("timeline", a);
				// Timeline.getTimeline(user_id);
				util_services.routeDisplays(debug,"out","user");
				return template.render(model);	
			}
			return "";
		});
		
		get("/follow", (req, res) -> {
			util_services.routeDisplays(debug,"in","follow");			
			String loggedin = req.session().attribute("loggedin");
			if(loggedin == null){
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug,"out","follow");
				res.redirect(redirectUrl);	
			} else {			
				int user_id=req.session().attribute("user_id");	
				String sql = "select handle,display_name,User.user_id from User where User.user_id not in (select target from Follow where Follow.user_id="+user_id+");";
				//String sql = "select Tweets.user_id, User.display_name, User.handle, tweet_msg, date_time FROM Tweets inner join User on Tweets.user_id = User.user_id  WHERE user.user_id = " + user_id + " ORDER BY date_time desc;";
				System.out.println("**** SQL for Follow = "+sql);
				ArrayList a = follow.selectTimeline(sql);
				JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/Follow.html");
				JtwigModel model = JtwigModel.newModel().with("followlist", a);
				util_services.routeDisplays(debug,"out","follow");
				return template.render(model);				
			}
			return "";
		});

		post("/follow_submit", (req, res) -> {
			util_services.routeDisplays(debug,"in","follow_submit");			
			String loggedin = req.session().attribute("loggedin");
			if(loggedin == null){
				System.out.println("not logged in");
				String redirectUrl = "/login";
				util_services.routeDisplays(debug,"out","follow_submit");
				res.redirect(redirectUrl);	
			} else {			
				int user_id=req.session().attribute("user_id");	
//				int search_id = Integer.parseInt(req.queryParams("search_id"));  
				int target_id = Integer.parseInt(req.queryParams("target_id"));
//				System.out.println("search id from search = "+search_id);
				System.out.println("target id from search = "+target_id);
				//String sql = "INSERT INTO  handle,display_name,User.user_id FROM User WHERE User.user_id not in (SELECT target FROM Follow WHERE Follow.user_id=23);";
				//String sql = "select Tweets.user_id, User.display_name, User.handle, tweet_msg, date_time FROM Tweets inner join User on Tweets.user_id = User.user_id  WHERE user.user_id = " + user_id + " ORDER BY date_time desc;";
				//System.out.println("**** SQL for Follow = "+sql);
				follow.addFollow(user_id, target_id);
				util_services.routeDisplays(debug,"out","follow_submit");			
			}
			return "";
		});
		
		
	}

}