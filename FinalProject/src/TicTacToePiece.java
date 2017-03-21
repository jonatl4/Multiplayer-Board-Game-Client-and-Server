import java.util.ArrayList;
import javafx.util.Pair;
/*
 * Inf 122 Final Project
 * Team Members:
 * Gen Fillipow
 * Jana Abumeri
 * Eva Ruiz
 * Adil rafaa
 * Jonathan  lee
 * David Diep
 * Seth Kruse
 * Brandon Truong*/
public class TicTacToePiece extends Piece{
	
	// Constructors
	public TicTacToePiece(){
		this.name = "BLANK";
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public TicTacToePiece(String name, Pair<Integer, Integer> currentPosition, ArrayList<String> possibleMoves){
		this.name = name;
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public TicTacToePiece(Piece tttPiece){
		this.name = tttPiece.name;
		this.currentPosition = tttPiece.currentPosition;
		this.possibleMoves.addAll(tttPiece.possibleMoves);
	}	
}
