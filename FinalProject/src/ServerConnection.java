import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import javax.swing.JOptionPane;

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

//import NetworkProtocol.ProtocolType;

public class ServerConnection extends Thread{
	
	Socket socket;
	int clientId;
	boolean running = true;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ClientLobby lobby;
	private NetworkProtocol outgoingData = null;
	private NetworkProtocol clientInput = null;
	private String gameType;
	private int matchId;
	private boolean waiting = true;//I created this to check for game restarts
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private User user;
	
	public ServerConnection(Socket socket, int clientId, ClientLobby lobby){
		this.matchId = 0;
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
	
	public String getGameType(){
		return gameType;
	}
	
	public void run(){
		
		while(running){
			try {
				clientInput = (NetworkProtocol)input.readObject();
				
				if(clientInput.getDataType() == NetworkProtocol.ProtocolType.STARTGAME){
					this.gameType = ((GameBoard) clientInput.getData()).getGameType();
					if(waiting){
						if(matchClients(clientId)){
							GameBoard gb1 = ((GameBoard) clientInput.getData());
							gb1.setTurn(true);
							Pair<ServerConnection, ServerConnection> activeGame = lobby.getActiveGame(matchId);
							activeGame.getKey().getUser().setUserToken(1);
							activeGame.getValue().getUser().setUserToken(2);
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.MAKEMOVE, gb1, activeGame.getKey().getUser());
							activeGame.getKey().sendPacket(outgoingData);
							GameBoard gb2 = ((GameBoard) clientInput.getData());
							gb2.setTurn(false);
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT, gb2, activeGame.getValue().getUser());
							activeGame.getValue().sendPacket(outgoingData);
							activeGame.getKey().setWaiting(false);
							activeGame.getValue().setWaiting(false);
						}
					}else{//This means a game has been restarted
						lobby.setClientThread(clientId, this);
						if(matchClients(clientId)){
							GameBoard gb1 = ((GameBoard) clientInput.getData());
							gb1.setTurn(true);
							Pair<ServerConnection, ServerConnection> activeGame = lobby.getActiveGame(matchId);
							activeGame.getKey().getUser().setUserToken(1);
							activeGame.getValue().getUser().setUserToken(2);
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.MAKEMOVE, gb1, activeGame.getKey().getUser());
							activeGame.getKey().sendPacket(outgoingData);
							GameBoard gb2 = ((GameBoard) clientInput.getData());
							gb2.setTurn(false);
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT, gb2, activeGame.getValue().getUser());
							activeGame.getValue().sendPacket(outgoingData);
						}
					}
				}else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.CLIENTMOVE){
					Pair<ServerConnection, ServerConnection> activeGame = lobby.getActiveGame(matchId);
					GameBoard gb1 = ((GameBoard) clientInput.getData());
					if(gb1.hasWinner() || gb1.noLegitMoves()){
						lobby.getActiveGames().remove(matchId);//Delete active games from the lobby
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.GAMEOVER, gb1);
						activeGame.getKey().sendPacket(outgoingData);
						activeGame.getValue().sendPacket(outgoingData);
					}else{
						if(gb1.getTurn()){
							gb1.setTurn(!gb1.getTurn());
							if(this.getClientId() == activeGame.getKey().getClientId()){
								outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT, gb1);
								activeGame.getKey().sendPacket(outgoingData);
								GameBoard gb2 = ((GameBoard) clientInput.getData());
								gb2.setTurn(!gb1.getTurn());
								outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.MAKEMOVE, gb2);
								activeGame.getValue().sendPacket(outgoingData);
							}else{
								outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT, gb1);
								activeGame.getValue().sendPacket(outgoingData);
								GameBoard gb2 = ((GameBoard) clientInput.getData());
								gb2.setTurn(!gb1.getTurn());
								outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.MAKEMOVE, gb2);
								activeGame.getKey().sendPacket(outgoingData);
							}
						}
					}
				}else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.WAIT){}
				else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.SAVERECORD){
					user = (User)clientInput.getData();
					User.resaveAccounts(user);
				}
				else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.ACCOUNT)
				{
					user = (User)clientInput.getData();
					if(User.validate(user))
					{
						user = User.retrieve(user.getUserName());
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTVALID);
						sendPacket(outgoingData);
						System.out.println("User valid: Account"); // valid user
					}
					else					
					{
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTINVALID);
						sendPacket(outgoingData);
						System.out.println("User invalid: Account"); // invalid user or password
					}					
				}
				else if(clientInput.getDataType() == NetworkProtocol.ProtocolType.NEWACCOUNT)
				{
					user = (User)clientInput.getData();
					
					if(User.exists(user)) // if account already exist
					{
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTINVALID);
						sendPacket(outgoingData);	
						System.out.println("User invalid - already taken: NewAccount : " + user.getUserName());
					}
					else
					{
						User.addAccount(user); // create new account
						User.saveAccounts();
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNTVALID);
						sendPacket(outgoingData);
						System.out.println("User added : NewAccount :" + user.getUserName());
					}
				}
				
			} catch (ClassNotFoundException e) {
				running = false;
				System.out.println("Something went wrong");
				e.printStackTrace();
			} catch (IOException e) {
				running = false;
				lobby.getClientThreads().remove(clientId);
				if(lobby.getActiveGames().containsKey(matchId)){
					lobby.getActiveGames().remove(matchId);
				}
				try{
					output.close();
					input.close();
					socket.close();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
				System.out.println("Disconnected");
			}
		}
	}
	
	private void sendPacket(NetworkProtocol packet)
	{
		try
		{
			output.writeObject(packet);
			output.flush();
		}
		catch(IOException ioe)
		{
			System.out.println("MESSAGE SEND ABORTED DUE TO SERVER DISCONNECT");
			
		}
	}
	
	public boolean matchClients(int clientId){
		int matchedWaitingClientId = 0;
		boolean foundGame = false;
		
		if(lobby.getClientThreads().size() > 1){
			for (Map.Entry<Integer, ServerConnection> clientMap : lobby.getClientThreads().entrySet()){
				if(clientMap.getKey() != clientId){
					if(((GameBoard) clientInput.getData()).getGameType().equals(clientMap.getValue().getGameType())){
						matchId = (int)(Math.random()*100);
						while(!lobby.gameExists(matchId)){
							if(!lobby.gameExists(matchId)){
								lobby.setActiveThread(matchId, new Pair<ServerConnection, ServerConnection>(this, clientMap.getValue()));
								matchedWaitingClientId = clientMap.getValue().getClientId();
								foundGame = true;
								clientMap.getValue().setMatchId(matchId);
								break;
							}else{
								matchId++;
							}
						}
					}else{
						System.out.println("this is false");
					}
				}
			}
			if(foundGame){
				lobby.getClientThreads().remove(clientId);
				lobby.getClientThreads().remove(matchedWaitingClientId);
			}
		}
		return foundGame;
	}
	
	public int getClientId(){
		return clientId;
	}
	
	public void setMatchId(int matchId){
		this.matchId = matchId;
	}
	
	public int getMatchId(){
		return matchId;
	}
	
	private void setWaiting(boolean flag){
		waiting = flag;
	}
	
}
