package twitterClone;

public class Utilities {

	
	public void routeDisplays(boolean debug, String direction, String path){
		if (debug){
			if(direction=="in"){
				System.out.println(">>>>entering "+"/"+path+" route");				
			} else{
				System.out.println("<<<<exiting "+"/"+path+" route");				
			}
			
		}

	}
}
