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
		TimeLine timeline = new TimeLine();

		get("/register", (req, res) -> {
			JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/Register.html");
			JtwigModel model = JtwigModel.newModel();
//			JtwigModel model = JtwigModel.newModel().with("timeline", a);
			// Timeline.getTimeline(user_id);
			return template.render(model);
		});

        post("/test", (req, res) -> {
//            System.out.print("/test ");
            String username = req.queryParams("username");
            String handle = req.queryParams("handle");
            String display_name = req.queryParams("displayName");
            String psw1 = req.queryParams("psw1");
            String psw2 = req.queryParams("psw2");
            User user = new User();
            user.insertUser(username, handle, display_name, psw1, psw2);
//            String second = req.queryParams("second");
//            try {
//                int a = Integer.parseInt(first);
////                int b = Integer.parseInt(second);
//                return new Integer(a + b).toString();
//            } catch (NumberFormatException ex) {
//                System.out.println("bad input");
//            }
//            return "failure";
            return "in test";
        });
		
		get("/timeLine", (req, res) -> {
			String sql = "SELECT tweet_id,user_id,tweet_msg,date_time FROM Tweets ORDER BY date_time desc";
			ArrayList a = timeline.selectTimeline(sql);
			JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
			JtwigModel model = JtwigModel.newModel().with("timeline", a);
			// Timeline.getTimeline(user_id);
			return template.render(model);
		});

		get("/user", (req, res) -> {
			String sql = "SELECT tweet_id,user_id,tweet_msg,date_time FROM Tweets WHERE user_id=2 ORDER BY date_time desc";
			ArrayList a = timeline.selectTimeline(sql);
			JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/TwitterClone.jtwig");
			JtwigModel model = JtwigModel.newModel().with("timeline", a);
			// Timeline.getTimeline(user_id);
			return template.render(model);
		});
		
		// get("/timeline", (req, res) -> {
		// JtwigTemplate template =
		// JtwigTemplate.classpathTemplate("TwitterClone.jtwig");
		// JtwigModel model = JtwigModel.newModel().with("albums", a);
		// return template.render(model);
		// });

		// Format display using Jtwig
		// get("/", (req, res) -> {
		// JtwigTemplate template =
		// JtwigTemplate.classpathTemplate("example.jtwig");
		// JtwigModel model = JtwigModel.newModel().with("albums",
		// albumList.albums);
		// return template.render(model);
		// });

		// Format using Json
		// get("/json", (req, res) -> {
		// Gson gson = new Gson();
		// System.out.println("In Json route. Json format = " +
		// gson.toJson(albumList));
		// return gson.toJson(albumList.albums);
		// });
		//
		// // Read Jsonformat and create HTML
		// get("/parsejson", (req, res) -> {
		// System.out.println("In Parse Json route");
		// JtwigTemplate template =
		// JtwigTemplate.classpathTemplate("example.jtwig");
		// JtwigModel model = JtwigModel.newModel().with("albums", albumList);
		// return template.render(model);
		// });
		//
		// // get("/album/:id", (req, res) -> {
		// // System.out.println("request made");
		// // System.out.println(req.queryParams("b"));
		// // return "hi world2";
		// // });
		//
		// get("/album/:id", (request, response) -> {
		// Album album =
		// albumList.getAlbum(Integer.parseInt(request.params(":id")));
		// if (album == null) {
		// return "Album was not found.";
		// }
		// return ("Hello: " + album.title + " " + album.artist + " " +
		// album.genre);
		// });
		//
		// get("/addAlbum/:title/:artist/:genre", (request, response) -> {
		// int newID = albumList.addAlbum(request.params(":title"),
		// request.params(":artist"),
		// request.params(":genre"));
		// return (newID);
		// });
	}

}