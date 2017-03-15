import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Client extends JFrame implements Runnable{
	
	private static Socket clientSocket = null;
	private static ObjectOutputStream output = null;
	private static ObjectInputStream input = null;
	protected User user;
	boolean isStillPlaying = true;
	
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
					//System.out.println("Receiving TESTSERVER data from server");

					
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.MATCHED){
						System.out.println("Matched");
				}
				else if(incoming.getDataType() == NetworkProtocol.ProtocolType.ACCOUNTVALID){
					System.out.println("Waiting for opponent...");
					TicTacToe gameBoard = new TicTacToe();
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.STARTGAME, (GameBoard) gameBoard);
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
}
