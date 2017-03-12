import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static ServerSocket server;
	private static boolean successfulConnection = true;
	private static int clientCount;
	private static ClientLobby lobby;
	
	public static void main(String[] args){
		try {
			System.out.println("Starting Server...");
			server = new ServerSocket(5000);
		} catch (IOException e) {
			successfulConnection = false;
			e.printStackTrace();
		}
		System.out.println("Server Started!");
		lobby = new ClientLobby();
		while(successfulConnection){
			try {
				System.out.println("Waiting for connection...");
				Socket connection = server.accept();
				clientCount++;
				ServerConnection newClient = new ServerConnection(connection, clientCount, lobby);
				lobby.setClientThread(clientCount, newClient);
				lobby.setActiveThread(clientCount, false);
				newClient.start();
			} catch (IOException e) {
				successfulConnection = false;
				e.printStackTrace();
			}
		}
	}
}
