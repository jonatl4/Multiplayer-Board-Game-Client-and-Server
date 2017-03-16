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
	private final JLabel message = new JLabel("TEST TEXT");
	
	private ImageIcon tictactoeX = new ImageIcon("images/x.png");
	private ImageIcon tictactoeO = new ImageIcon("images/o.png");
	
	public ClientGUI(String host, int port) {
		chooseGame();
		initializeGUI();
	}
	
	public void initializeGUI() {
		// Frame
		super.setTitle("Board Game Client GUI");
		this.setSize(800, 850);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		// Top HUD
		JPanel topPanel = new JPanel();
		topPanel.add(message);
		contentPane.add(topPanel, BorderLayout.PAGE_START);
		
		// Board 
		contentPane.add(gui, BorderLayout.CENTER);
		if (currGame.getGameType().equals("TICTACTOE")) {
			gui.setLayout(new GridLayout(0, 3));
		} else {
			gui.setLayout(new GridLayout(0, 8));
		}
		
		// Buttons
		int i;
		int j;
		
		grid = currGame.getCurrentGameBoardState();
		
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
		if(currGame != null) {
			String command = ae.getActionCommand();
			int row = Integer.parseInt("" + command.charAt(0));
			int col = Integer.parseInt("" + command.charAt(1));
			Dimension dim = new Dimension(row,col);
			NetworkProtocol move = new NetworkProtocol(NetworkProtocol.ProtocolType.MAKEMOVE, dim);
			
			try {
				output.writeObject(move);
				System.out.println("Move sent.");
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, "\nConnection to Server Lost - Closing");
				System.exit(0);
			}
		}
	}
	
	public void updateBoard(GameBoard update) {
		int i;
		int j;
		ImageIcon icon = new ImageIcon();
		
		if (currGame.getGameType().equals("TICTACTOE")) {
			currGame = update;
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
		}
	}
	public void chooseGame() {
		Object[] games = {"Tic Tac Toe", "Checkers", "Othello"};
		int choice = JOptionPane.showOptionDialog(null, "Choose a game:", 
				"Game Selection", JOptionPane.DEFAULT_OPTION,	
				JOptionPane.WARNING_MESSAGE, null, games, games[0]);
		if (choice == 0) {
			currGame = new TicTacToe();
		} else if (choice == 1) {
			// board = new Checkers();
		} else if (choice == 2) {
			// board = new Othello();
		}

	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
