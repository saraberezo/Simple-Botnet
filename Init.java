import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Init {

	public static void main(String[] args) {
		
		// VARIABLES
		Scanner sc = new Scanner(System.in);
		String url = null;
		HttpURLConnection connection = null;
		
		System.out.println("Url (http://www.url.webdomain): ");
		url = sc.nextLine().trim();
		
		// Close input flow (scanner) -> No more data will be asked
		sc.close();
		 
		 
		 // With this code you can test the web initial state
		 while (true) {
	        try {
	            long start = System.currentTimeMillis();
	            URL u = new URL(url);
	            connection = (HttpURLConnection) u.openConnection();
	            connection.setRequestMethod("GET");
	 
	            int code = connection.getResponseCode();
	            long end = System.currentTimeMillis();
	            System.out.println("Code: " + code + " t:" +(end-start));
	       } catch (MalformedURLException e) {
	                System.out.println("URL error: " +e);
	                break;
	       } catch (IOException e) {
	                System.out.println("Connecting error: " +e);
	                break;
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	            }
	        }
		 }
}
		

}
