import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class ServerConnection extends Thread{
	
	Socket socket;
	int clientId;
	boolean running = true;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ClientLobby lobby;
	
	public ServerConnection(Socket socket, int clientId, ClientLobby lobby){
		this.socket = socket;
		this.clientId = clientId;
		this.lobby = lobby;
		try{
			OutputStream o = socket.getOutputStream();
			output = new ObjectOutputStream(o);
			InputStream i = socket.getInputStream();
			input = new ObjectInputStream(i);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		NetworkProtocol outgoingData = null;
		while(running){
			try {
				NetworkProtocol clientInput = (NetworkProtocol)input.readObject();
				if(clientInput.getDataType() == NetworkProtocol.ProtocolType.TESTCLIENT){
					System.out.println("Validating Account for "+String.valueOf(clientId));
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.TESTSERVER);
					output.writeObject(outgoingData);
					output.flush();
				}
			} catch (ClassNotFoundException | IOException e) {
				running = false;
				System.out.println("Something went wrong");
				e.printStackTrace();
			}
		}
	}
	
	public void matchClients(int clientId){
		if(lobby.getClientThreads().size() > 1){
			
		}
	}
	
	public int getClientId(){
		return clientId;
	}
	
}
