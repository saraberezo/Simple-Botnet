import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ThreadBot implements Runnable {

	//-------------------------------- VARIABLES --------------------------------//
	
	String url = null;
	
	//-------------------------------- CONSTRUCTORS --------------------------------//
	
		public ThreadBot(String new_request) {
			
			url = new_request;
			
		}
	
	//-------------------------------- METHODS --------------------------------//
	
	public void run() {
	
		HttpURLConnection connection = null;
		
		while (true) {
			try {
				long start = System.currentTimeMillis();
	            URL u = new URL(url);
	            connection = (HttpURLConnection) u.openConnection();
	            connection.setRequestMethod("HEAD");
	 
	            int code = connection.getResponseCode();
	            long end = System.currentTimeMillis();
	            System.out.println("Code: " + code + " t:" +(end-start));
			} catch (MalformedURLException MUe) {
	                System.out.println("URL error: " + MUe);
			} catch (IOException IOe) {
	                System.out.println("Connecting error: " + IOe);
			} finally {
				if (connection != null)
					connection.disconnect();
			}
		}
		
		
	}

}
