package twitterClone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
