import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;
import javafx.util.Pair;

public class Checkers extends GameBoard implements Serializable, Cloneable
{
	private int numRows = 8;
	private int numColumns = 8;
	private Pair<Integer, Integer> selection;
	
	/* CheckersPiece.name could = 
	 * 	'r' = normal red pieces
	 * 	'rk' = red king pieces
	 * 	'b' = normal black pieces
	 *	'bk' = black king pieces
	 * ---------------------------
	 * 	Player1 = Red
	 * 	Player2 = Black
	 * */
	
	public Checkers()
	{
		for (int i = 0; i< this.numRows; i++ ){ //puts checkerPiece on the gameboard
			for(int j = 0; j < this.numColumns; j++){
				this.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new CheckersPiece());
				}
			}
		for (int i = 1; i < this.numColumns; i += 2){
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 1)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 1), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 1)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 5)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 5), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 5)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 7)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 7), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 7)));
		}
		for(int i = 0; i < this.numColumns; i += 2){
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 0)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 0), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 0)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 2)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 2), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 2)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 6)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 6), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 6)));
		}				
		this.pieceSelected = new CheckersPiece(); //piece selected is initially BLANK
		this.gameType = "CHECKERS"; // will relate to the client later
		this.selection = new Pair<Integer, Integer>(0,0); // default
		this.turn = false; // default
	}
	
	public Checkers(Checkers c){
		this.numRows = c.numRows;
		this.numColumns = c.numColumns;
		this.currentGameBoardState.putAll(c.currentGameBoardState);
		this.gameType = c.gameType;
		this.pieceSelected = new CheckersPiece(c.pieceSelected);
		this.selection = c.selection;
		this.turn = c.turn;
	}
	
	public boolean moveSequence(Pair<Integer, Integer> move, Piece piece, int userToken)
	{
		int x = move.getKey();
		int y = move.getValue();
		boolean jumpCheck = jumpPossible();
		
		if(this.pieceSelected.getName().equals("BLANK")){
			if(this.currentGameBoardState.get(move).getName().equals("BLANK")){
				return false; 
			}
			if((this.currentGameBoardState.get(move).getName().startsWith("r") && userToken == 1) 
				|| (this.currentGameBoardState.get(move).getName().startsWith("b") && userToken == 2))
			{
				if(!jumpCheck || (jumpCheck && canJump(move))){
					this.pieceSelected = new CheckersPiece(this.currentGameBoardState.get(move));
					selection = move;
					return true;
				}
			}
			else{
				return false; 
			}
		}
		else{
			if(x-selection.getValue()==0 && y-selection.getKey()==0) // undo selection
			{
				pieceSelected.setName("BLANK");				
				return true;
			}
			
			if(!this.currentGameBoardState.get(move).getName().equals("BLANK")){
				return false; 
			}
			
			int moveType = legitMove(move, jumpCheck, userToken);
			
			if(moveType > 0){
				boolean kinged = ((userToken == 1 && y == 0) || (userToken == 2 && y == 7));
				if(userToken ==1){
					this.currentGameBoardState.get(move).setName("r");
				}
				else if(userToken == 2){
					this.currentGameBoardState.get(move).setName("b");
				}
				
				this.currentGameBoardState.get(selection).setName("BLANK");
				
				if(!kinged){
					this.currentGameBoardState.replace(move, pieceSelected);
					pieceSelected.setCurrentPosition(move);
				}
				else{
					if(userToken == 1){
						this.currentGameBoardState.get(move).setName("rk");
						this.currentGameBoardState.get(move).setPossibleMoves(new ArrayList<String>(Arrays.asList("backward")));
						this.currentGameBoardState.get(move).setCurrentPosition(move);
					}
					else if(userToken == 2){
						this.currentGameBoardState.get(move).setName("bk");
						this.currentGameBoardState.get(move).setPossibleMoves(new ArrayList<String>(Arrays.asList("backward")));
						this.currentGameBoardState.get(move).setCurrentPosition(move);
					}
				}
				
				this.currentGameBoardState.get(selection).setName("BLANK");
				
				if(moveType == 1){
					this.pieceSelected.setName("BLANK");
					this.turn = false;
				}
				else if(moveType == 2){
					this.currentGameBoardState.get(new Pair<Integer, Integer>((selection.getKey()+2)/2, (selection.getValue()+2)/2)).setName("BLANK");
					
					if(!kinged && canJump(move)){
						Pair<Integer, Integer> temp = new Pair<Integer, Integer>(y,x);
						selection = temp;
					}
				}
				else{
					this.pieceSelected.setName("BLANK");
					this.turn = false; 
				}
				
				return true;
			}
		}
		
		return false; //change this when done
	}
	
	public int legitMove(Pair<Integer, Integer> move, boolean jumpRequired, int userToken)
	{
		int direction = 0;
		String opponent = "";
		int x = move.getKey();
		int y = move.getValue();
		
		if(userToken == 1){ // 'r' player
			direction = -1; 
			opponent = "b";
		} 
		else if(userToken == 2){ // 'b' player
			direction = 1; 
			opponent = "r";
		}
		if(!jumpRequired){
			if(this.currentGameBoardState.get(move).getName().equals("BLANK") && y == selection.getKey()+direction &&
				(x==selection.getValue()+1 || x==selection.getValue()-1))
			{
				return 1;
			}
			if(this.pieceSelected.getName().equals("rk") || this.pieceSelected.getName().equals("bk")){
				if(this.currentGameBoardState.get(move).getName().equals("BLANK") && y == selection.getKey()-direction &&  
					(x==selection.getValue()+1 || x==selection.getValue()-1))
				{
					return 1; 
				}
			}
		}
		if(this.currentGameBoardState.get(move).getName().equals("BLANK") && y == selection.getKey()+(direction*2) && 
			((x==selection.getValue()+2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()+direction, selection.getValue()+1)).getName().equals(opponent)) 
			|| (x==selection.getValue()-2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()+direction, selection.getValue()-1)).getName().equals(opponent))))
		{
				
			return 2; 
		}
		
		if(this.pieceSelected.getName().equals("rk") || this.pieceSelected.getName().equals("bk")){
			if(this.currentGameBoardState.get(move).getName().equals("BLANK") && y==selection.getKey()-(direction*2) &&
				((x==selection.getValue()+2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()-direction, selection.getValue()+1)).getName().equals(opponent))
				|| (x==selection.getValue()-2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()-direction, selection.getValue()-1)).getName().equals(opponent))))
			{
				return 2; 
			}
		}
		
		return 0;
	}
	
	public boolean jumpPossible(){
		for (int i = 0; i< this.numRows; i++ ){ //puts checkerPiece on the gameboard
			for(int j = 0; j < this.numColumns; j++){
				if(this.turn){
					if(canJump(new Pair<Integer, Integer>(i,j))){
						return true;
					}
				}
			}
		}
		
		return false; 
	}
	
	private boolean canJump(Pair<Integer, Integer> pair) {
		// TODO Auto-generated method stub
		int x = pair.getKey();
		int y = pair.getValue();
		if(this.currentGameBoardState.get(pair).getName().equals("r")){
			if(x > 1 && y > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-1,x-1)).getName().startsWith("b") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-2,x-2)).getName().equals("BLANK")){
				return true;
			}
			if(x < 6 && y > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-1,x+1)).getName().startsWith("b") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-2,x+2)).getName().equals("BLANK")){
				return true;
			}
		}
		else if(this.currentGameBoardState.get(pair).getName().equals("b")){
			if(x > 1 && y < 6 &&  
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+1,x-1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+2,x-2)).getName().equals("BLANK")){
				return true;
			}
			if(x < 6 && y < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+1,x+1)).getName().startsWith("b") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+2,x+2)).getName().equals("BLANK")){
				return true;
			}
		}
		else if(this.currentGameBoardState.get(pair).getName().equals("bk") || this.currentGameBoardState.get(pair).getName().equals("rk")){
			String opponent = ""; 
			
			if(this.currentGameBoardState.get(pair).getName().equals("rk")){
				opponent = "b";
			}
			else{
				opponent = "r";
			}
			
			if(x > 1 && y > 1 &&  
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-1,x-1)).getName().startsWith(opponent) &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-2,x-2)).getName().equals("BLANK")){
				return true;
			}
			if(x < 6 && y > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-1,x+1)).getName().startsWith(opponent) &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y-2,x+2)).getName().equals("BLANK")){
				return true;
			}
			if(x > 1 && y < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+1,x-1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+2,x-2)).getName().equals("BLANK")){
				return true;
			}
			if(x < 6 && y < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+1,x+1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (y+2,x+2)).getName().equals("BLANK")){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void resetBoard() {
		// TODO Auto-generated method stub
		for (int i = 0; i< this.numRows; i++ ){ //puts checkerPiece on the gameboard
			for(int j = 0; j < this.numColumns; j++){
				this.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new CheckersPiece());
			}
		}
		for (int i = 1; i < this.numColumns; i += 2){
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 1)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 1), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 1)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 5)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 5), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 5)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 7)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 7), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 7)));
		}
		for(int i = 0; i < this.numColumns; i += 2){
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 0)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 0), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 0)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 2)).setName("r");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 2), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 2)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 6)).setName("b");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(i, 6), this.currentGameBoardState.get(new Pair<Integer, Integer>(i, 6)));
		}				
		
		this.pieceSelected = new CheckersPiece(); //piece selected is initially BLANK
		this.turn = false; // default		
	}
	
	@Override
	public int checkWinner() {
		// TODO Auto-generated method stub
		int numRed = 0;
		int numBlack = 0;
		
		for(int i = 0; i < this.numRows; i++){
			for(int j = 0; j < this.numColumns; j++){
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("r") 
					|| this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("rk")){
					numRed++;
				}
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("b") 
					|| this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("bk")){
					numBlack++;
				}
			}
		}
		
		if(numBlack == 0)
			return 1;
		else if(numRed == 0)
			return 2; 
		else 
			return 0; // tie or no winner
	}
	
	@Override
	public boolean hasWinner() {
		// TODO Auto-generated method stub
		return checkWinner() == 0 ? false : true;
	}
	@Override
	public GameBoard clone() {
		// TODO Auto-generated method stub
		GameBoard clonedBoard = new Checkers();
		
		for(int i = 0; i < this.numRows; i++){
			for(int j = 0; j < this.numColumns; j++){
				clonedBoard.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)));
			}
		}
		
		clonedBoard.turn = this.turn;
		clonedBoard.pieceSelected = new CheckersPiece(this.pieceSelected);
		clonedBoard.gameType = this.gameType;
		return (GameBoard)clonedBoard;
	}
	@Override
	public void noLegitMoves() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void printBoard() {
		// TODO Auto-generated method stub	
		System.out.println("  0 1 2 3 4 5 6 7 x");
		for(int i = 0; i < this.numRows; i++){
			System.out.print(i + " ");
		    for(int j = 0; j < this.numColumns; j++){
		    	System.out.print(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName() + " ");
		    }
		    System.out.println();
		}
		System.out.println("y");
	}
	
	public void executeMove(){
		
	}
	
 }
