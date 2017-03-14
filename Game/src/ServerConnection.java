import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import javax.swing.JOptionPane;

//import NetworkProtocol.ProtocolType;

public class ServerConnection extends Thread{
	
	Socket socket;
	int clientId;
	boolean running = true;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ClientLobby lobby;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private User user;
	
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
					//System.out.println("Validating Account for "+String.valueOf(clientId));
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.TESTSERVER);
					output.writeObject(outgoingData);
					output.flush();					
				}
				else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.ACCOUNT)
				{
					user = (User)clientInput.getData();
					if(User.validate(user))
					{
						user = User.retrieve(user.getUserName());
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTVALID);
						output.writeObject(outgoingData);
						output.flush();	
						System.out.println("User valid: Account"); // valid user
					}
					else					
					{
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTINVALID);
						output.writeObject(outgoingData);
						output.flush();	
						System.out.println("User invalid: Account"); // invalid user or password
					}					
				}
				else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.NEWACCOUNT)
				{
					user = (User)clientInput.getData();
					
					if(User.exists(user)) // if account already exist
					{
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTINVALID);
						output.writeObject(outgoingData);
						output.flush();	
						System.out.println("User invalid - already taken: NewAccount : " + user.getUserName());
					}
					else
					{
						User.addAccount(user); // create new account
						User.saveAccounts();
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTVALID);
						output.writeObject(outgoingData);
						output.flush();	
						System.out.println("User added : NewAccount :" + user.getUserName());
					}
				}
				
				
				
				
				
				
			} catch (ClassNotFoundException e) {
				running = false;
				System.out.println("Something went wrong");
				e.printStackTrace();
			} catch (IOException e) {
				running = false;
				System.out.println("Problem with grabbing input");
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
