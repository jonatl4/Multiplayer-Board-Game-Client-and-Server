import java.util.HashMap;
import java.util.Scanner;

import javafx.util.Pair;

public class main {
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		int x = 0;
		int y = 0;
		
		User player1 = new User("jen", "pass");
		player1.setUserToken(1);
		User player2 = new User("eva", "pass");
		player2.setUserToken(2);
		
		TicTacToe gameBoardP1 = new TicTacToe(); // player1's board
		TicTacToe gameBoardP2 = new TicTacToe(); // player1's board
		
		// start with player1's turn 
		gameBoardP1.setTurn(true); // default value = false 
		printBoard(gameBoardP1);
		
		while(true){
			if((gameBoardP1.boardFilledUp()) || gameBoardP1.hasWinner())
				break;
			
			System.out.println("Pick a position on the board to make a move: ");
			System.out.print("Enter x coordinate: ");
			x = scanner.nextInt();
			System.out.print("Enter y coordinate: ");
			y = scanner.nextInt();
			System.out.println("");
			
			Pair<Integer, Integer> move = new Pair<Integer, Integer>(x,y);
			
			if(gameBoardP1.turn && gameBoardP1.moveSequence(move, new TicTacToePiece(), player1.getUserToken())){ // tictactoepiece = dummy var
				gameBoardP1.setTurn(false);
				gameBoardP2 = new TicTacToe(gameBoardP1);
				gameBoardP2.setTurn(true);
				printBoard(gameBoardP1);
				System.out.println("\nPlayer 2's turn");
			}else if(gameBoardP2.turn && gameBoardP2.moveSequence(move, new TicTacToePiece(), player2.getUserToken())){
				gameBoardP2.setTurn(false);
				gameBoardP2 = new TicTacToe(gameBoardP1);
				gameBoardP1.setTurn(true);
				printBoard(gameBoardP2);
				System.out.println("\nPlayer 1's turn");
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
	
	public static void printBoard(TicTacToe gameBoard){
		// row 1
		System.out.println(
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name + "|");
		// row 2
		System.out.println(
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name + "|");
		
		// row 3
		System.out.println(
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name + "|" +
				gameBoard.currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name + "|\n");
	} 
}
