package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Utilities {

	public void routeDisplays(boolean debug, String direction, String path) {
		if (debug) {
			if (direction == "in") {
				System.out.println(">>>>entering " + "/" + path + " route");
			} else {
				System.out.println("<<<<exiting " + "/" + path + " route");
			}
		}
	}

	public void dbInitCheck(TwitterDAL twitterDAL, String[] args) {
	      
		if (args.length > 0) {
			
//			String dbInitParm = args[0].toLowerCase();
			if (args[0].equalsIgnoreCase("init")) {			
			      Scanner in = new Scanner(System.in);			      
			      System.out.println("Are you sure you want to initialize your database (Y/N)?");
			      String resp = in.nextLine();
			      if (resp.equalsIgnoreCase("Y")){
						System.out.println("Initializing Twitter Database...");
						twitterDAL.deleteDB();
						twitterDAL.createDB();  
			      }
			} else {
				System.out.println(args[0]+ " not recognized. If you want to initialize "
						+ "the database enter init as the runtime argument.");
			}
		}
	}

}
