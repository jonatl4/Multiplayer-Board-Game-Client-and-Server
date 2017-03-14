import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Server {
	
	private static ServerSocket server;
	private static boolean successfulConnection = true;
	private static int clientCount;
	private static ClientLobby lobby;
	private static ObjectOutputStream input = null;
	private static ObjectInputStream output = null;

	
	
	public static void main(String[] args){
		
		/* added */
		User.loadAccounts();
		
		try {
			server = new ServerSocket(4999);
			
		} catch (IOException e) {
			successfulConnection = false;
			e.printStackTrace();
		}
		System.out.println("Server Started!");
		lobby = new ClientLobby();
		
		
		while(successfulConnection){
			try {
				
				
				//System.out.println("Waiting for connection...");
				
				
				Socket connection = server.accept();
				clientCount++;
				ServerConnection newClient = new ServerConnection(connection, clientCount, lobby);
				
				lobby.setClientThread(clientCount, newClient);
				lobby.setActiveThread(clientCount, false);
				lobby.setCThreads(newClient.getUser(), clientCount);
				newClient.start();
			

				
				
			} catch (IOException e) {
				successfulConnection = false;
				e.printStackTrace();
			}
		}
	}
}
