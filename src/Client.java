import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Client extends JFrame implements Runnable, ActionListener{
	
	protected static Socket clientSocket = null;
	private static ObjectOutputStream output = null;
	private static ObjectInputStream input = null;
	protected static User user;
	boolean isStillPlaying = true;
	protected static GameBoard currGame;
	
	//GUI Stuff
//	private ClientGUI gui;
	private Map<Pair<Integer,Integer>, Piece> grid;
	private Container contentPane = this.getContentPane();
	private JPanel gui = new JPanel();
	private JPanel topPanel = new JPanel();
	private JButton[][] buttons = new JButton[8][8];
	private JLabel message;
	private JLabel statsLabel;
	
	private ImageIcon tictactoeX = new ImageIcon(getClass().getResource("x.png"));
	private ImageIcon tictactoeO = new ImageIcon(getClass().getResource("o.png"));
	
	private ImageIcon othelloB = new ImageIcon(getClass().getResource("blackOthello.png"));
	private ImageIcon othelloW = new ImageIcon(getClass().getResource("whiteOthello.png"));
	//GUI Stuff
	
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
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.MAKEMOVE){
					if(incoming.getUser() != null){
						user = (User) incoming.getUser();
						JOptionPane.showMessageDialog(this, "Start Game! You're " + currGame.getPieceType(user.getUserToken()));
						statsLabel.setText("Win-Loss Record: " + user.getRecord("Wins") + " - " + user.getRecord("Losses"));
					}
					System.out.println("Make your move");
					currGame = ((GameBoard) incoming.getData());
					updateBoard();
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.WAIT){
					if(incoming.getUser() != null){
						user = (User) incoming.getUser();
						System.out.println(user.getUserToken());
						JOptionPane.showMessageDialog(this, "Start Game! You're " + currGame.getPieceType(user.getUserToken()));
						statsLabel.setText("Win-Loss Record: " + user.getRecord("Wins") + " - " + user.getRecord("Losses"));
					}
					currGame = ((GameBoard) incoming.getData());
					System.out.println("Waiting for opponent to make move");
					updateBoard();					
					outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.WAIT);
					sendPacket(outgoingData);
				}else if(incoming.getDataType() == NetworkProtocol.ProtocolType.GAMEOVER){
					currGame = ((GameBoard) incoming.getData());
					int winner = currGame.checkWinner();
					if(winner == 1){
						if(user.getUserToken() == winner){
							int wins = user.getRecord("Wins");
							wins++;
							user.saveRecord("Wins", wins);
							updateBoard();
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.SAVERECORD, user);
							sendPacket(outgoingData);
							JOptionPane.showMessageDialog(this, "You Won!");
							playAgain();
						}else{
							int losses = user.getRecord("Losses");
							losses++;
							user.saveRecord("Losses", losses);
							updateBoard();
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.SAVERECORD, user);
							sendPacket(outgoingData);
							JOptionPane.showMessageDialog(this, "Sorry, you lost...");
							playAgain();
						}
					}else if(winner == 2){
						if(user.getUserToken() == winner){
							int wins = user.getRecord("Wins");
							wins++;
							user.saveRecord("Wins", wins);
							updateBoard();
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.SAVERECORD, user);
							sendPacket(outgoingData);
							JOptionPane.showMessageDialog(this, "You Won!");
							playAgain();
						}else{
							int losses = user.getRecord("Losses");
							losses++;
							user.saveRecord("Losses", losses);
							updateBoard();
							outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.SAVERECORD, user);
							sendPacket(outgoingData);
							JOptionPane.showMessageDialog(this, "Sorry, you lost...");
							playAgain();
						}
					}else{
						updateBoard();
						JOptionPane.showMessageDialog(this, "Draw");
						System.out.println("Draw!");
						playAgain();
					}
				}
				else if(incoming.getDataType() == NetworkProtocol.ProtocolType.ACCOUNTVALID){
					System.out.println("Waiting for opponent...");
					currGame = chooseGame();
					initializeGUI();
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
	
	public void playAgain(){
		int option = JOptionPane.showConfirmDialog(this, "Play again?", "BestGameServer", JOptionPane.YES_NO_OPTION);
		if(option == JOptionPane.YES_OPTION){
			gui.removeAll();
			topPanel.removeAll();
			currGame = chooseGame();
			initializeGUI();
			NetworkProtocol outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.STARTGAME, currGame);
			sendPacket(outgoingData);
		}else{
			System.exit(0);
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(currGame.getTurn()){
			String command = ae.getActionCommand();
			Integer row = Integer.parseInt("" + command.charAt(0));
			Integer col = Integer.parseInt("" + command.charAt(1));
			Pair<Integer, Integer> currMove = new Pair<Integer, Integer>(row, col);
			System.out.println("Move: " + String.valueOf(currMove.getKey()) + ", " + String.valueOf(currMove.getValue()));
			if(currGame.moveSequence(currMove, currGame.newGamePiece(), user.getUserToken())){
				NetworkProtocol outgoingData = new NetworkProtocol(NetworkProtocol.ProtocolType.CLIENTMOVE, currGame);
				sendPacket(outgoingData);
			}
		}
	}
	
	public GameBoard chooseGame() {
		Object[] games = {"Tic Tac Toe", "Checkers", "Othello"};
		int choice = JOptionPane.showOptionDialog(null, "Choose a game:", 
				"Game Selection", JOptionPane.DEFAULT_OPTION,	
				JOptionPane.WARNING_MESSAGE, null, games, games[0]);
		if (choice == 0) {
			return new TicTacToe();
		} else if (choice == 1) {
			// board = new Checkers();
		} else if (choice == 2) {
			return new Othello();
		}
		return null;
	}
	
	protected void sendPacket(NetworkProtocol packet)
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
	
	public void initializeGUI() {
		// Frame
		super.setTitle("Board Game Client GUI");
		this.setSize(800, 850);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		// Top HUD
		message = new JLabel("Player " + user.getUserName());
		topPanel.add(message);
		contentPane.add(topPanel, BorderLayout.PAGE_START);

		
		// Buttons
		int i;
		int j;
		
		// Board 
		contentPane.add(gui, BorderLayout.CENTER);
		if (currGame.getGameType().equals("TICTACTOE")) {
			gui.setLayout(new GridLayout(0, 3));
		} else {
			gui.setLayout(new GridLayout(0, 8));
		}
		
		grid = currGame.getCurrentGameBoardState();
		Comparator<Pair<Integer, Integer>> comparator = new Comparator<Pair<Integer, Integer>>() {
			public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
				int result;
			    if((p1.getKey().compareTo(p2.getKey()) == 0)){
			    	result = p1.getValue().compareTo(p2.getValue());
			    }else{
			    	result = p1.getKey().compareTo(p2.getKey());
			    }
			    return result;
			}
		};
		
		TreeMap<Pair<Integer,Integer>, Piece> sortedMap = new TreeMap<Pair<Integer,Integer>, Piece>(comparator);
		sortedMap.putAll(grid);
		
		for (Map.Entry<Pair<Integer, Integer>, Piece> entry : sortedMap.entrySet()) {
			i = entry.getKey().getKey(); // x coordinate
			j = entry.getKey().getValue(); // y coordinate
			buttons[i][j] = new JButton("");
			buttons[i][j].addActionListener(this);
			buttons[i][j].setActionCommand("" + i + "" + j);
			if(currGame.getGameType().equals("OTHELLO")){
				buttons[i][j].setBackground(Color.GREEN);
				buttons[i][j].setOpaque(true);
				buttons[i][j].setBorderPainted(true);
			}
			gui.add(buttons[i][j]);
		}
		
		// Bottom HUD
		JPanel botPanel = new JPanel();
		JPanel statsPanel = new JPanel();
		statsLabel = new JLabel("Win-Loss Record: wait until connected...");
		statsPanel.add(statsLabel);
		botPanel.add(statsPanel, BorderLayout.WEST);
		contentPane.add(botPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public ImageIcon scaleImg(ImageIcon icon, JButton button) {
		Image img = icon.getImage();
		Image simg = img.getScaledInstance( button.getWidth(), button.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(simg);
		return icon;
	}
	
	public void updateBoard() {
		int i;
		int j;
		ImageIcon icon = new ImageIcon();
		
		if(currGame.getTurn()){
			message.setText("Player " + user.getUserName() + " it's your move");
		}else{
			message.setText("Waiting for your opponenet to move");
		}

		if (currGame.getGameType().equals("TICTACTOE")) {
			grid = currGame.getCurrentGameBoardState();
			
			for (Map.Entry<Pair<Integer, Integer>, Piece> entry : grid.entrySet()) {
				i = entry.getKey().getKey(); // x coordinate
				j = entry.getKey().getValue(); // y coordinate
				
				Piece piece = currGame.getPiece(entry.getKey());
				if (piece.getName().equals("X")) {
					icon = scaleImg(tictactoeX, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				} else if (piece.getName().equals("O")) {
					icon = scaleImg(tictactoeO, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				}
			}
		}else if(currGame.getGameType().equals("OTHELLO")){
			grid = currGame.getCurrentGameBoardState();
			
			for (Map.Entry<Pair<Integer, Integer>, Piece> entry : grid.entrySet()){
				i = entry.getKey().getKey();
				j = entry.getKey().getValue();
				
				Piece piece = currGame.getPiece(entry.getKey());
				if(piece.getName().equals("black")){
					icon = scaleImg(othelloB, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				}else if(piece.getName().equals("white")){
					icon = scaleImg(othelloW, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				}
			}
		}
	}
}
