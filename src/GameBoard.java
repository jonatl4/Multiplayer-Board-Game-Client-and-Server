import java.awt.Dimension;
import java.io.Serializable;
import java.util.*;
import javafx.util.Pair; 

/*
 * Jana Abumeri, Eva Ruiz
 * GameBoard class
 * INF 122 - Team 4, Final Project  
 * */

public abstract class GameBoard extends GameObject {
	protected HashMap<Pair<Integer,Integer>, Piece> currentGameBoardState;
	protected ArrayList<Piece> pieces;
	
	protected String gameType; // the game type that this board represents
	protected boolean turn; // whose turn it is - the user?
	protected Piece pieceSelected;  
	protected boolean moveInProgress; 
	
	
	// Setters & getters
	public Piece getPiece(Pair<Integer, Integer> position){
		return currentGameBoardState.get(position);
	}
	
	public HashMap<Pair<Integer,Integer>, Piece> getCurrentGameBoardState() {
		return currentGameBoardState;
	}
	
	public void setCurrentGameBoardState(HashMap<Pair<Integer,Integer>, Piece> currentGameBoardState) {
		this.currentGameBoardState = currentGameBoardState;
	}
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public boolean getTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public Piece getPieceSelected() {
		return pieceSelected;
	}

	public void setPieceSelected(Piece pieceSelected) {
		this.pieceSelected = pieceSelected;
	}

	public boolean isMoveInProgress() {
		return moveInProgress;
	}

	public void setMoveInProgress(boolean moveInProgress) {
		this.moveInProgress = moveInProgress;
	}

	public abstract void resetBoard();
	public abstract boolean moveSequence(Pair<Integer, Integer> move, Piece piece, int userToken);
	public abstract int checkWinner();
	public abstract boolean hasWinner();
	public abstract GameBoard clone();
	public abstract boolean noLegitMoves();

}