import java.util.*;
import javafx.util.Pair;

/*
 * Jana Abumeri, Eva Ruiz
 * GameBoard class
 * INF 122 - Team 4, Final Project
 * 
 *   Abstract game Piece class. Contains the name of the piece (i.e. "x"), 
 *   it's current position in the board, and an ArrayList of it's possible
 *   moves (i.e. Queen in Chess can move in any direction - left, right, etc.)
 *   	i.e. name = "DarkKing"
 *   		 currentePosition = <1,1>
 *   		 possibleMoves = ["diagonal in all directions"] 
 * */


public abstract class Piece {
	public String name;
	public Pair<Integer, Integer> currentPosition; 
	public ArrayList<String> possibleMoves = new ArrayList<String>();
	
	// Setters & getters 
//	public abstract String getName();
//	public abstract void setName(String name);
//	public abstract Pair<Integer, Integer> getCurrentPosition();
//	public abstract void setCurrentPosition(Pair<Integer, Integer> currentPosition);
//	public abstract ArrayList<String> getPossibleMoves();
//	public abstract void setPossibleMoves(ArrayList<String> possibleMoves);
	
	// Setters & getters 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Pair<Integer, Integer> getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(Pair<Integer, Integer> currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	public ArrayList<String> getPossibleMoves() {
		return possibleMoves;
	}
	public void setPossibleMoves(ArrayList<String> possibleMoves) {
		this.possibleMoves.addAll(possibleMoves);
	}

}
