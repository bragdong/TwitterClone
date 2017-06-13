package twitterClone;

import static spark.Spark.get;
import static spark.Spark.port;
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
		port(3000);
//		AlbumList albumList = new AlbumList();

		// Format display method in class
		 get("/", (req, res) -> {
		 return "Hello World";
		 });

		// Format display using Jtwig
		// get("/", (req, res) -> {
		// JtwigTemplate template =
		// JtwigTemplate.classpathTemplate("example.jtwig");
		// JtwigModel model = JtwigModel.newModel().with("albums",
		// albumList.albums);
		// return template.render(model);
		// });

		// Format using Json
//		get("/json", (req, res) -> {
//			Gson gson = new Gson();
//			System.out.println("In Json route. Json format = " + gson.toJson(albumList));
//			return gson.toJson(albumList.albums);
//		});
//
//		// Read Jsonformat and create HTML
//		get("/parsejson", (req, res) -> {
//			System.out.println("In Parse Json route");
//			JtwigTemplate template = JtwigTemplate.classpathTemplate("example.jtwig");
//			JtwigModel model = JtwigModel.newModel().with("albums", albumList);
//			return template.render(model);
//		});
//
//		// get("/album/:id", (req, res) -> {
//		// System.out.println("request made");
//		// System.out.println(req.queryParams("b"));
//		// return "hi world2";
//		// });
//
//		get("/album/:id", (request, response) -> {
//			Album album = albumList.getAlbum(Integer.parseInt(request.params(":id")));
//			if (album == null) {
//				return "Album was not found.";
//			}
//			return ("Hello: " + album.title + " " + album.artist + " " + album.genre);
//		});
//
//		get("/addAlbum/:title/:artist/:genre", (request, response) -> {
//			int newID = albumList.addAlbum(request.params(":title"), request.params(":artist"),
//					request.params(":genre"));
//			return (newID);
//		});
	}

}