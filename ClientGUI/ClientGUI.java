import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLDocument.Iterator;

import javafx.util.Pair;

public class ClientGUI extends JFrame implements ActionListener, Runnable {
	
	private HashMap<Pair<Integer,Integer>, String> grid = new HashMap<Pair<Integer,Integer>, String>();
	private Container contentPane = this.getContentPane();
	private JPanel gui = new JPanel();
	private final JLabel message = new JLabel("TEST TEXT");
	
	public ClientGUI(String host, int port) {
		populateHashMap();
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
		gui.setLayout(new GridLayout(0, 8));
		
		// Buttons
		int i;
		int j;
		JButton[][] buttons = new JButton[8][8];
		
		for (Map.Entry<Pair<Integer, Integer>, String> entry : grid.entrySet()) {
			i = entry.getKey().getKey();
			j = entry.getKey().getValue();
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
	
	public void populateHashMap() {
		for (int i=0; i<8; i++)
		{
			for (int j=0; j<8; j++)
			{
				Pair<Integer,Integer> pair = new Pair<Integer, Integer>(i, j);
				grid.put(pair, "EMPTY");
			}
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();
		int row = Integer.parseInt("" + command.charAt(0));
		int col = Integer.parseInt("" + command.charAt(1));
		Dimension dim = new Dimension(row,col);
	}
	
	public GameBoard chooseGame() {
		GameBoard board = null;
		Object[] games = {"Tic Tac Toe", "Checkers", "Othello"};
		int choice = JOptionPane.showOptionDialog(null, "Choose a game:", 
				"Game Selection", JOptionPane.DEFAULT_OPTION,	
				JOptionPane.WARNING_MESSAGE, null, games, games[0]);
		if(choice == 0) {
			board = new TicTacToe();
		}
		
		return board;
	}
	
	public static void main(String[] args) {
		ObjectOutputStream oos = null;
		String host = "localhost";
		int port = 8000;
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				ClientGUI gui = new ClientGUI(host, port);
			}
		};
		SwingUtilities.invokeLater(r);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
