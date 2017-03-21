import java.util.ArrayList;
import javafx.util.Pair;

public class CheckersPiece extends Piece{
	
	// Constructors
	public CheckersPiece(){
		this.name = "BLANK";
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public CheckersPiece(String name, Pair<Integer, Integer> currentPosition, ArrayList<String> possibleMoves){
		this.name = name;
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public CheckersPiece(Piece cPiece){
		this.name = cPiece.name;
		this.currentPosition = cPiece.currentPosition;
		if(!cPiece.possibleMoves.isEmpty())
			this.possibleMoves.addAll(cPiece.possibleMoves);
	}	
}