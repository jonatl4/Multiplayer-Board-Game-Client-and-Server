import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;
/*
 * Inf 122 Final Project
 * Team Members:
 * Gen Fillipow
 * Jana Abumeri
 * Eva Ruiz
 * Adil rafaa
 * Jonathan  lee
 * David Diep
 * Seth Kruse
 * Brandon Truong*/
public class ClientLobby {

	private Map<Integer, ServerConnection> clientThreads;
	private Map<Integer, Pair<ServerConnection, ServerConnection>> activeGames;
	
	public ClientLobby(){
		clientThreads = new HashMap<Integer, ServerConnection>();
		activeGames = new HashMap<Integer, Pair<ServerConnection, ServerConnection>>();
	}
	
	public Map<Integer, ServerConnection> getClientThreads(){
		return clientThreads;
	}
	
	public Map<Integer, Pair<ServerConnection, ServerConnection>> getActiveGames(){
		return activeGames;
	}
	
	public ServerConnection getClient(int clientId){
		return clientThreads.get(clientThreads);
	}
	
	public Pair<ServerConnection, ServerConnection> getActiveGame(int gameId){
		return activeGames.get(gameId);
	}
	
	public void setClientThread(int clientId, ServerConnection clientThread){
		clientThreads.put(clientId, clientThread);
	}
	
	public void setActiveThread(int gameId, Pair<ServerConnection, ServerConnection> activeGame){
		activeGames.put(gameId, activeGame);
	}
	
	public boolean gameExists(int matchId){
		return activeGames.containsKey(matchId);
	}
}