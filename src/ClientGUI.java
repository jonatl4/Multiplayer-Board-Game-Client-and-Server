import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLDocument.Iterator;

import javafx.util.Pair;

public class ClientGUI extends Client implements ActionListener, Runnable {
	
	private HashMap<Pair<Integer,Integer>, Piece> grid = new HashMap<Pair<Integer,Integer>, Piece>();
	private Container contentPane = this.getContentPane();
	private JPanel gui = new JPanel();
	private JButton[][] buttons = new JButton[8][8];
	private JLabel message;
	
	private ImageIcon tictactoeX = new ImageIcon(getClass().getResource("x.png"));
	private ImageIcon tictactoeO = new ImageIcon(getClass().getResource("o.png"));
	
	public ClientGUI() {
	}
	
	public void initializeGUI(GameBoard gameBoard) {
		// Frame
		super.setTitle("Board Game Client GUI");
		this.setSize(800, 850);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		// Top HUD
		JPanel topPanel = new JPanel();
		message = new JLabel("Player " + user.getUserName());
		topPanel.add(message);
		contentPane.add(topPanel, BorderLayout.PAGE_START);
		
		// Board 
		contentPane.add(gui, BorderLayout.CENTER);
		if (gameBoard.getGameType().equals("TICTACTOE")) {
			gui.setLayout(new GridLayout(0, 3));
		} else {
			gui.setLayout(new GridLayout(0, 8));
		}
		
		// Buttons
		int i;
		int j;
		
		grid = gameBoard.getCurrentGameBoardState();
		
		for (Map.Entry<Pair<Integer, Integer>, Piece> entry : grid.entrySet()) {
			i = entry.getKey().getKey(); // x coordinate
			j = entry.getKey().getValue(); // y coordinate
			buttons[i][j] = new JButton("");
			buttons[i][j].addActionListener(this);
			buttons[i][j].setActionCommand("" + i + "" + j);
			gui.add(buttons[i][j]);
		}
		
		// Bottom HUD
		JPanel botPanel = new JPanel();
		JPanel statsPanel = new JPanel();
		JLabel statsLabel = new JLabel("Win-Loss Record: GAH!");
		statsPanel.add(statsLabel);
		botPanel.add(statsPanel, BorderLayout.WEST);
		contentPane.add(botPanel, BorderLayout.SOUTH);
	}
	
	public ImageIcon scaleImg(ImageIcon icon, JButton button) {
		Image img = icon.getImage();
		Image simg = img.getScaledInstance( button.getWidth(), button.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(simg);
		return icon;
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
	
	public void updateBoard(GameBoard game) {
		int i;
		int j;
		ImageIcon icon = new ImageIcon();

		if (game.getGameType().equals("TICTACTOE")) {
			grid = game.getCurrentGameBoardState();
			
			for (Map.Entry<Pair<Integer, Integer>, Piece> entry : grid.entrySet()) {
				i = entry.getKey().getKey(); // x coordinate
				j = entry.getKey().getValue(); // y coordinate
				
				Piece piece = game.getPiece(entry.getKey());
				if (piece.getName().equals("X")) {
					icon = scaleImg(tictactoeX, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				} else if (piece.getName().equals("0")) {
					icon = scaleImg(tictactoeO, buttons[i][j]);
					buttons[i][j].setIcon(icon);
				}
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
			// board = new Othello();
		}
		return new TicTacToe();

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
