import java.util.HashMap;
import java.util.Map;

public class ClientLobby {

	private Map<Integer, ServerConnection> clientThreads;
	private Map<Integer, Boolean> activeThreads;
	
	public ClientLobby(){
		clientThreads = new HashMap<Integer, ServerConnection>();
		activeThreads = new HashMap<Integer, Boolean>();
	}
	
	public Map<Integer, ServerConnection> getClientThreads(){
		return clientThreads;
	}
	
	public Map<Integer, Boolean> getActiveThreads(){
		return activeThreads;
	}
	
	public ServerConnection getClient(int clientId){
		return clientThreads.get(clientThreads);
	}
	
	public Boolean getActiveThread(int clientId){
		return activeThreads.get(clientId);
	}
	
	public void setClientThread(int clientId, ServerConnection clientThread){
		clientThreads.put(clientId, clientThread);
	}
	
	public void setActiveThread(int clientId, Boolean check){
		activeThreads.put(clientId, check);
	}
}
