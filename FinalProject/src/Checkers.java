import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
		this.gameType = "CHECKERS"; // will relate to the client later
		this.selection = new Pair<Integer, Integer>(0,0); // default
		this.pieceSelected = new CheckersPiece(); //piece selected is initially BLANK
		resetBoard();
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
	
	public void setSelection(Pair<Integer, Integer> selection){
		this.selection = selection;
	}
	
	public Pair<Integer, Integer> getSelection(){
		return this.selection;
	}
	
	public boolean moveSequence(Pair<Integer, Integer> move, Piece piece, int userToken)
	{
		int x = move.getKey(); // width
 		int y = move.getValue(); // height
		boolean jumpCheck = jumpPossible(userToken);
		
		if(this.pieceSelected.getName().equals("BLANK")){//Checks if the user has already selected a piece
			//if we get here then all the program knows is that the user hasn't selected a piece yet
			if(this.currentGameBoardState.get(move).getName().equals("BLANK")){//Here the program checks if the the move the user selected is blank
				return false;//this tells the program that the user has selected a blank space for the first space
			}
			if((this.currentGameBoardState.get(move).getName().startsWith("r") && userToken == 1) 
				|| (this.currentGameBoardState.get(move).getName().startsWith("b") && userToken == 2))//checks if the first piece the user selected is a valid piece
			{
				if(!jumpCheck || (jumpCheck && canJump(move))){//if there are possible jumps on the board check for that if not, this will still return true
					this.pieceSelected = new CheckersPiece();//This shows that the user has officially selected a piece
					if(this.currentGameBoardState.get(move).getName().equals("red")){
						this.pieceSelected.setName("red");
					}else if (this.currentGameBoardState.get(move).getName().equals("black")){
						this.pieceSelected.setName("black");
					}else if (this.currentGameBoardState.get(move).getName().equals("rk")){
						this.pieceSelected.setName("rk");
					}else if (this.currentGameBoardState.get(move).getName().equals("bk")){
						this.pieceSelected.setName("bk");
					}
					
					this.pieceSelected.setCurrentPosition(move);
					System.out.println("Piece Selected: " + this.pieceSelected.getCurrentPosition());
					selection = move;//This saves the Pair<X, Y> object so the code can reference it later
					return false;//finally returns true although in our case we might want to return false maybe use a boolean variable called gameStateChanged
				}
			}
			else{
				return false; 
			}
		}
		else{//So if we get to here we know the program knows that the user has selected its' piece it wants to move
			System.out.println("Piece Selected: " + pieceSelected.getCurrentPosition());
			if(x-selection.getKey()==0 && y-selection.getValue()==0) //If we click the piece again it'll undo the selected checkers piece so we're free to choose another one
			{
				pieceSelected.setName("BLANK");				
				return false;//Again, maybe user the gamestateChange variable here
			}
			
			if(!this.currentGameBoardState.get(move).getName().equals("BLANK")){//This checks if the second space you selected already has a piece there
				return false; 
			}
			
			System.out.println("jump check " + String.valueOf(jumpCheck));
			int moveType = legitMove(move, jumpCheck, userToken);//1 for regular move and 2 for jumping move and 0 for invalid move
			System.out.println(moveType);
			if(moveType > 0){
				boolean kinged = ((userToken == 1 && x == 0) || (userToken == 2 && x == 7));
				if(userToken == 1){
					this.currentGameBoardState.get(move).setName("red");
					this.currentGameBoardState.get(move).setCurrentPosition(move);//Set all the appropriate info where the new piece is being placed
//					System.out.println(this.currentGameBoardState.get(move).getName());
//					System.out.println(this.currentGameBoardState.get(move).getCurrentPosition());
				}
				else if(userToken == 2){
					this.currentGameBoardState.get(move).setName("black");
					this.currentGameBoardState.get(move).setCurrentPosition(move);
					System.out.println(this.currentGameBoardState.get(move).getName());
				}
				
				this.currentGameBoardState.get(selection).setName("BLANK");//Takes the initially selected piece and turns it to a blank spot
				System.out.println("CHECK THIS1 " + this.currentGameBoardState.get(move).getName());
				
				if(!kinged){
					System.out.println("Piece Selected: " + pieceSelected.getCurrentPosition());
					Piece tempPiece = new CheckersPiece(pieceSelected);
					this.currentGameBoardState.replace(move, tempPiece);
				}
				else{
					ArrayList<String> possibleMoves = new ArrayList<String>();
					possibleMoves.add("backwards");
					if(userToken == 1){
						this.currentGameBoardState.get(move).setName("rk");
						this.currentGameBoardState.get(move).setPossibleMoves(possibleMoves);
						this.currentGameBoardState.get(move).setCurrentPosition(move);
					}
					else if(userToken == 2){
						this.currentGameBoardState.get(move).setName("bk");
						this.currentGameBoardState.get(move).setPossibleMoves(possibleMoves);
						this.currentGameBoardState.get(move).setCurrentPosition(move);
					}
				}
				
				System.out.println("CHECK THIS2 " + this.currentGameBoardState.get(move).getName());
				
				if(moveType == 1){
					this.currentGameBoardState.get(pieceSelected.getCurrentPosition()).setName("BLANK");
					this.pieceSelected.setName("BLANK");
					System.out.println("CHECK THIS3 " + this.currentGameBoardState.get(move).getName());
				}
				if(moveType == 2){
					System.out.println("here");
					System.out.println(selection);
					System.out.println(move);
					System.out.println("here");
					int fromX = selection.getKey();
					int fromY = selection.getValue();
					int toX = move.getKey();
					int toY = move.getValue();
					
					if(toY > fromY && toX > fromX){
						this.currentGameBoardState.get(new Pair<Integer, Integer>(++fromX, ++fromY)).setName("BLANK");
					}
					if(toY > fromY && toX < fromX){
						this.currentGameBoardState.get(new Pair<Integer, Integer>(--fromX, ++fromY)).setName("BLANK");
					}
					if(toY < fromY && toX > fromX){
						this.currentGameBoardState.get(new Pair<Integer, Integer>(++fromX, --fromY)).setName("BLANK");
					}
					if(toY < fromY && toX < fromX){
						this.currentGameBoardState.get(new Pair<Integer, Integer>(--fromX, --fromY)).setName("BLANK");
					}
					

					if(!kinged && canJump(move)){
						System.out.println("here");
						Pair<Integer, Integer> temp = new Pair<Integer, Integer>(x,y);
						selection = temp;
					}else{
						this.currentGameBoardState.get(pieceSelected.getCurrentPosition()).setName("BLANK");
						this.pieceSelected.setName("BLANK");
					}
				}
//				System.out.println("CHECK THIS4 " + this.currentGameBoardState.get(move).getName());
				return true;
			}
		}
		return false; //change this when done
	}
	
	public int legitMove(Pair<Integer, Integer> move, boolean jumpRequired, int userToken)//1 means its a legal regular move and 2 means its a legal jumping move
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
			System.out.println("its player b turn");
		}
		if(!jumpRequired){//If we don't need to jump over a piece then we enter this if statement
			if(this.currentGameBoardState.get(move).getName().equals("BLANK") && x == selection.getKey()+direction &&
				(y==selection.getValue()+1 || y==selection.getValue()-1))//If the place you wanna put a piece is blank and the direction matech with the move you gave it(based on the player) then it will return 1
			{
				return 1;
			}
			if(this.pieceSelected.getName().equals("rk") || this.pieceSelected.getName().equals("bk")){//If the piece selected is a king then they can move the other direction
				if(this.currentGameBoardState.get(move).getName().equals("BLANK") && x == selection.getKey()-direction &&  
					(y==selection.getValue()+1 || y==selection.getValue()-1))
				{
					return 1; 
				}
			}
		}
		if(this.currentGameBoardState.get(move).getName().equals("BLANK") && x == selection.getKey()+(direction*2) && //This is for jumping over a piece
			((y==selection.getValue()+2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()+direction, selection.getValue()+1)).getName().startsWith(opponent)) 
			|| (y==selection.getValue()-2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()+direction, selection.getValue()-1)).getName().startsWith(opponent))))
		{
				
			return 2; 
		}
		
		if(this.pieceSelected.getName().equals("rk") || this.pieceSelected.getName().equals("bk")){
			if(this.currentGameBoardState.get(move).getName().equals("BLANK") && x==selection.getKey()-(direction*2) &&
				((y==selection.getValue()+2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()-direction, selection.getValue()+1)).getName().startsWith(opponent))
				|| (y==selection.getValue()-2 && this.currentGameBoardState.get(new Pair<Integer, Integer>(selection.getKey()-direction, selection.getValue()-1)).getName().startsWith(opponent))))
			{
				return 2; 
			}
		}
		
		return 0;
	}
	
	public boolean jumpPossible(int userToken){
		for (int i = 0; i< this.numRows; i++ ){ //puts checkerPiece on the gameboard
			for(int j = 0; j < this.numColumns; j++){
				if((this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().startsWith("r") && userToken == 1) 
					|| (this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().startsWith("b") && userToken == 2)){
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
		if(this.currentGameBoardState.get(pair).getName().equals("red")){
			if(x > 1 && y > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-1,y-1)).getName().startsWith("b") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-2,y-2)).getName().equals("BLANK")){
				return true;
			}
			if(y < 6 && x > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-1,y+1)).getName().startsWith("b") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-2,y+2)).getName().equals("BLANK")){
				return true;
			}
		}
		else if(this.currentGameBoardState.get(pair).getName().equals("black")){
			if(y > 1 && x < 6 &&  
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+1,y-1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+2,y-2)).getName().equals("BLANK")){
				return true;
			}
			if(y < 6 && x < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+1,y+1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+2,y+2)).getName().equals("BLANK")){
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
			
			if(y > 1 && x > 1 &&  
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-1,y-1)).getName().startsWith(opponent) &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-2,y-2)).getName().equals("BLANK")){
				return true;
			}
			if(y < 6 && x > 1 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-1,y+1)).getName().startsWith(opponent) &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x-2,y+2)).getName().equals("BLANK")){
				return true;
			}
			if(y > 1 && x < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+1,y-1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+2,y-2)).getName().equals("BLANK")){
				return true;
			}
			if(y < 6 && x < 6 && 
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+1,y+1)).getName().startsWith("r") &&
					this.currentGameBoardState.get(new Pair<Integer, Integer> (x+2,y+2)).getName().equals("BLANK")){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void resetBoard() {
		// TODO Auto-generated method stub
		this.currentGameBoardState = new HashMap<Pair<Integer,Integer>, Piece>();
		for (int i = 0; i< this.numRows; i++ ){ //puts checkerPiece on the gameboard
			for(int j = 0; j < this.numColumns; j++){
				this.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new CheckersPiece());
			}
		}
		for (int i = 0; i < this.numColumns; i += 2){
			this.currentGameBoardState.get(new Pair<Integer, Integer>(7, i)).setName("red");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(7, i), this.currentGameBoardState.get(new Pair<Integer, Integer>(7, i)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(6, i+1)).setName("red");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(6, i+1), this.currentGameBoardState.get(new Pair<Integer, Integer>(6, i+1)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(5, i)).setName("red");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(5, i), this.currentGameBoardState.get(new Pair<Integer, Integer>(5, i)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(0, i+1)).setName("black");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(0, i+1), this.currentGameBoardState.get(new Pair<Integer, Integer>(0, i+1)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(1, i)).setName("black");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(1, i), this.currentGameBoardState.get(new Pair<Integer, Integer>(1, i)));
			this.currentGameBoardState.get(new Pair<Integer, Integer>(2, i+1)).setName("black");
			this.currentGameBoardState.replace(new Pair<Integer, Integer>(2, i+1), this.currentGameBoardState.get(new Pair<Integer, Integer>(2, i+1)));
		}
		this.turn = false; // default		
	}
	
	@Override
	public int checkWinner() {
		// TODO Auto-generated method stub
		int numRed = 0;
		int numBlack = 0;
		
		for(int i = 0; i < this.numRows; i++){
			for(int j = 0; j < this.numColumns; j++){
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("red") 
					|| this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("rk")){
					numRed++;
				}
				if(this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).getName().equals("black") 
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
	public boolean noLegitMoves() {
		// TODO Auto-generated method stub
		return hasWinner();
	}

	@Override
	public Piece newGamePiece() {
		// TODO Auto-generated method stub
		return new CheckersPiece();
	}

	@Override
	public String getPieceType(int userToken) {
		if(userToken == 1){
			return "red";
		}else{
			return "black";
		}
	}
	
 }
