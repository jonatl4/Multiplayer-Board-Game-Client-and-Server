import java.awt.Dimension;
import java.util.HashMap;

import javafx.util.Pair;

@SuppressWarnings("serial")
public class TicTacToe extends GameBoard{

	private int numRows; 
	private int numColumns;
	
	// constructors 
	public TicTacToe(){
		// instantiate TICTACTOE gameBoard & temp TICTACTOE gameBoard
		this.numRows = 3;
		this.numColumns = 3; 
		this.currentGameBoardState = new HashMap<Pair<Integer,Integer>, Piece>();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){ // board filled with BLANK pieces
				this.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new TicTacToePiece());
				//temp.put(new Pair<Integer, Integer>(i,j), new TicTacToePiece());
			}
		}
		this.gameType = "TICTACTOE"; // will relate to the client later
		this.turn = false; // default
	}
	
	public TicTacToe(TicTacToe ttt){
		this.numRows = ttt.numRows;
		this.numColumns = ttt.numColumns;
		this.currentGameBoardState.putAll(ttt.currentGameBoardState);
		this.gameType = ttt.gameType;
		this.turn = ttt.turn;
	}
	
	// setters & getters 
	@Override
	public void resetBoard() {
		// TODO Auto-generated method stub
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new TicTacToePiece());
				//temp.put(new Pair<Integer, Integer>(i,j), new TicTacToePiece());
			}
		}
		
		turn = false;
	}

	@Override
	public boolean moveSequence(Pair<Integer, Integer> move, Piece piece, int userToken) {
		// TODO Auto-generated method stub
		
		if(!boardFilledUp() && legalMove(move, piece)){
			//currentGameBoardState
			placeMove(move); 
			markCell(move, userToken);
			return true; 
		}
		
		return false; // dummy value
	}

	private void markCell(Pair<Integer, Integer> move, int userToken) {
		// TODO Auto-generated method stub
		if((move.getKey() >= 0 && move.getKey() <= 2) && (move.getValue() >= 0 && move.getValue() <= 2)){
			if(userToken == 1){
				this.currentGameBoardState.get(move).name = "X";
			}
			else if(userToken == 2){
				this.currentGameBoardState.get(move).name = "0";
			}
			this.currentGameBoardState.get(move).currentPosition = move;
		}
	}


	@Override // either "", "X", "O"
	public int checkWinner() {
		// TODO Auto-generated method stub
		// rows  
		if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("X")) ? 1 : 2;
		}
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name.equals("X")) ? 1 : 2;
		}
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name.equals("X")) ? 1 : 2;
		}
		// columns
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("X")) ? 1 : 2;
		}
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,1)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,1)).name.equals("X")) ? 1 : 2;
		}
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,2)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals("X")) ? 1 : 2;
		}
		// diagonals 
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,2)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,0)).name.equals("X")) ? 1 : 2;
		}
		else if((!currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals("BLANK") &&
		   !currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name.equals("BLANK")) &&
		(currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name) && 
		currentGameBoardState.get(new Pair<Integer, Integer>(1,1)).name.equals(currentGameBoardState.get(new Pair<Integer, Integer>(2,0)).name)))
		{
			return (currentGameBoardState.get(new Pair<Integer, Integer>(0,2)).name.equals("X")) ? 1 : 2;
		}
		else{
			return 0;
		}
	}

	public  boolean hasWinner(){
		return checkWinner() == 0 ? false : true; 
	}
	
	@Override
	public GameBoard clone() {
		// TODO Auto-generated method stub
		GameBoard clonedBoard = new TicTacToe();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				clonedBoard.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)));
			}
		}
		
		clonedBoard.turn = this.turn;
		clonedBoard.pieceSelected = this.pieceSelected;
		clonedBoard.gameType = this.gameType;
		
		return (GameBoard)clonedBoard; 
	}

	@Override
	public boolean noLegitMoves() {
		for(int i = 0; i < this.numRows; i++){
			for(int j = 0; j < this.numColumns; j++){
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).name.equals("BLANK")){
					return false; 
				}
			}
		}
		
		return true; 
	}
	
	public boolean boardFilledUp(){
		for(int i = 0; i < this.numRows; i++){
			for(int j = 0; j < this.numColumns; j++){
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).name.equals("BLANK")){
					return false; 
				}
			}
		}
		
		return true; 
	}
	
	public boolean legalMove(Pair<Integer, Integer> move, Piece piece){
		if(this.turn == true && this.currentGameBoardState.get(move).name.equals("BLANK")){
			return true; 
		}
		return false; 
	}
	
	public boolean placeMove(Pair<Integer, Integer> move){
		if(!this.currentGameBoardState.get(move).name.equals("BLANK")){
			return false; 
		}
			
		return true; 
	}
}
