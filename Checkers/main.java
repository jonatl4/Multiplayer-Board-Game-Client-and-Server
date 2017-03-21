import java.util.HashMap;
import java.util.Scanner;

import javafx.util.Pair;

public class main {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		int xFrom = 0;
		int xTo = 0;
		int yFrom = 0;
		int yTo = 0;
		
		User player1 = new User("jen", "pass");
		player1.setUserToken(1);
		User player2 = new User("eva", "pass");
		player2.setUserToken(2);
		
//		TicTacToe gameBoardP1 = new TicTacToe(); // player1's board
//		TicTacToe gameBoardP2 = new TicTacToe(); // player1's board
		
		Checkers gameBoardP1 = new Checkers();
		Checkers gameBoardP2 = new Checkers();
		
		// start with player1's turn 
		gameBoardP1.setTurn(true); // default value = false 
		gameBoardP1.printBoard();
		System.out.println("\nPlayer 1's turn ('r')");
		
		while(true){
			if(gameBoardP1.hasWinner())
				break;
			
			System.out.println("Pick a position on the board to make a move FROM: ");
			System.out.print("Enter x coordinate: ");
			xFrom = scanner.nextInt();
			System.out.print("Enter y coordinate: ");
			yFrom = scanner.nextInt();
			System.out.println("");
			
			Pair<Integer, Integer> selection = new Pair<Integer, Integer>(xFrom, yFrom);
			
			gameBoardP1.pieceSelected = new CheckersPiece(gameBoardP1.currentGameBoardState.get(selection));
			gameBoardP1.setSelection(selection);
			
			gameBoardP2.pieceSelected = new CheckersPiece(gameBoardP2.currentGameBoardState.get(selection));
			gameBoardP2.setSelection(selection);
			
			System.out.println("Pick a position on the board to make a move TO: ");
			System.out.print("Enter x coordinate: ");
			xTo = scanner.nextInt();
			System.out.print("Enter y coordinate: ");
			yTo = scanner.nextInt();
			System.out.println("");
			
			Pair<Integer, Integer> move = new Pair<Integer, Integer>(xTo, yTo);
			
			if(gameBoardP1.turn && gameBoardP1.moveSequence(move, new CheckersPiece(), player1.getUserToken())){ // tictactoepiece = dummy var
				gameBoardP1.setTurn(false);
				gameBoardP2 = new Checkers(gameBoardP1);
				gameBoardP2.setTurn(true);
//				System.out.println(gameBoardP1.getCurrentGameBoardState().get(new Pair<Integer, Integer>(4, 3)).getName());
//				System.out.println(gameBoardP2.getCurrentGameBoardState().get(new Pair<Integer, Integer>(4, 3)).getName());
				gameBoardP1.printBoard();
				System.out.println("\nPlayer 2's turn ('b')");
			}else if(gameBoardP2.turn && gameBoardP2.moveSequence(move, new CheckersPiece(), player2.getUserToken())){
				gameBoardP2.setTurn(false);
				gameBoardP2 = new Checkers(gameBoardP1);
				gameBoardP1.setTurn(true);
				gameBoardP2.printBoard();
				System.out.println("\nPlayer 1's turn ('r')");
			}
		}
		
		if(gameBoardP1.hasWinner()){ // determine winner
			if(gameBoardP1.checkWinner() == player1.getUserToken())
				System.out.println("\nPlayer 1 wins!");
			else
				System.out.println("\nPlayer 2 wins!");
		}
		else{
			System.out.println("\nIt's a tie!");
		}
		
		scanner.close();
	}
}
