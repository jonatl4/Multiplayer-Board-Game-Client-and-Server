import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.util.Pair;

public class Client extends JFrame implements Runnable{
	
	private static Socket clientSocket = null;
	private static ObjectOutputStream output = null;
	private static ObjectInputStream input = null;
	protected User user;
	boolean isStillPlaying = true;
	private GameBoard currGame;
	
	Scanner scanner = new Scanner(System.in);
	int x = 0;
	int y = 0;
	
	public static void main(String[] args){
		//System.out.println("Starting client connection...");
		try {
			clientSocket = new Socket("127.0.0.1", 4999);
			
			OutputStream o = clientSocket.getOutputStream();
			output = new ObjectOutputStream(o);
			
			InputStream i = clientSocket.getInputStream();
			input = new ObjectInputStream(i);
			
			
			
			Thread clientThread = new Thread(new Client());
			clientThread.start();
			
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		NetworkProtocol outgoingData = null;
		
		login();
		
		while(isStillPlaying){
			try {
				NetworkProtocol incoming = (NetworkProtocol)input.readObject();
				if(incoming.getDataType() == NetworkProtocol.ProtocolType.TESTSERVER){
					System.out.println("Player " + user.getUserName()+ " Wins!");
					//System.out.println("Receiving TESTSERVER data from server");
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.MAKEMOVE){
					if(incoming.getUser() != null){
						this.user = (User) incoming.getUser();
						System.out.println(this.user.getUserToken());
					}
					currGame = ((GameBoard) incoming.getData());
					printBoard(currGame);
					System.out.println("Make your move");
					/*Change Game State*/
					System.out.println("Pick a position on the board to make a move: ");
					System.out.print("Enter y coordinate: ");
					x = scanner.nextInt();
					System.out.print("Enter x coordinate: ");
					y = scanner.nextInt();
					System.out.println("");
					Pair<Integer, Integer> move = new Pair<Integer, Integer>(x,y);
					if(currGame.moveSequence(move, new TicTacToePiece(), this.user.getUserToken())){//Instead of TicTacToePiece() its gonna need to be more like new gamePieceBuilder() so it allows for plugins
						outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.CLIENTMOVE, currGame);
						sendPacket(outgoingData);
					}
//					System.out.println(currGame.getTurn());
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.WAIT){
					if(incoming.getUser() != null){
						this.user = (User) incoming.getUser();
						System.out.println(this.user.getUserToken());
					}
					currGame = ((GameBoard) incoming.getData());
					System.out.println("Waiting for opponent to make move");
					printBoard(currGame);
					//System.out.println(currGame.getTurn());
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT);
					sendPacket(outgoingData);
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.GAMEOVER){
					currGame = ((GameBoard) incoming.getData());
					int winner = currGame.checkWinner();
					if(winner == 1){
						if(user.getUserToken() == winner){
							printBoard(currGame);
							System.out.println("You Won!");
						}else{
							printBoard(currGame);
							System.out.println("Sorry, you lost...");
						}
					}else if(winner == 2){
						if(user.getUserToken() == winner){
							printBoard(currGame);
							System.out.println("You won!");
						}else{
							printBoard(currGame);
							System.out.println("Sorry, you lost...");
						}
					}
				}
				else if(incoming.getDataType() == NetworkProtocol.ProtocolType.ACCOUNTVALID){
					System.out.println("Waiting for opponent...");
					currGame = new TicTacToe();
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.STARTGAME, currGame);
					sendPacket(outgoingData);
				}
				else if(incoming.getDataType() == NetworkProtocol.ProtocolType.ACCOUNTINVALID){;
					login();
				}				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				isStillPlaying = false;
				JOptionPane.showMessageDialog(this, "SERVER CRASHED!");
				
			}
		}
	}
	
	private void sendPacket(NetworkProtocol packet)
	{
		// Send out the network packet in the argument.
		// Helper functions of the following function.
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
	
	public void login()
	{
		// begin login() process
		boolean packetSent = false;
		while(!packetSent)
		{
			Object[] options = {"Existing Account", "New Account"};
			int selection = JOptionPane.showOptionDialog(null, "Select an account type:\n" +
					"(if this is coming up again, the previous login failed)", 
					"GameBoard Account Login", JOptionPane.DEFAULT_OPTION,	
					JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			if(selection == 0)
			{
				String newUser = JOptionPane.showInputDialog("Enter your user name:");
				String pass = JOptionPane.showInputDialog("Enter your password:");
				user = new User(newUser, pass);
				NetworkProtocol clientOutput = new  NetworkProtocol(NetworkProtocol.ProtocolType.ACCOUNT,  user);
				sendPacket(clientOutput);				
				packetSent=true;	
			}
			else if(selection == 1)
			{
				String newuser = JOptionPane.showInputDialog("Enter a new user name:");
				String newpass1 = JOptionPane.showInputDialog("Enter a password:");
				String newpass2 = JOptionPane.showInputDialog("Retype the password:");
				if(newpass1.equals(newpass2))
				{
					user = new User(newuser, newpass1);
					NetworkProtocol clientOutput = new  NetworkProtocol(NetworkProtocol.ProtocolType.NEWACCOUNT,  user);
					sendPacket(clientOutput);
					packetSent=true;
				}
				else
				{
					JOptionPane.showInputDialog(this, "Your passwords do not match up!");
				}
			}
			
		}	
	}
	
	public static void printBoard(GameBoard gameBoard){
		// row 1
		System.out.println(
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(0,0)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(0,1)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(0,2)).name + "|");
		// row 2
		System.out.println(
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(1,0)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(1,1)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(1,2)).name + "|");
		
		// row 3
		System.out.println(
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(2,0)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(2,1)).name + "|" +
				gameBoard.getCurrentGameBoardState().get(new Pair<Integer, Integer>(2,2)).name + "|\n");
	} 
}
