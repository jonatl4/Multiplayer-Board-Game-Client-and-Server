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
@SuppressWarnings("serial")
public class Othello extends GameBoard{


	private int numRows; 
	private int numColumns;
	
	/** A constant representing an empty square on the board. */
	  private static final String NO_CHIP = " ";
	  /** A constant representing a black peg on the board. */
	  private static final String BLACK_UP = "black";
	  /** A constant representing a white peg on the board. */
	  private static final String WHITE_UP = "white";
	  /** A constant indicating the size of the game board. */

	  private String userColor = BLACK_UP;
	  
	  public void inverseColor(){
		  if(userColor == BLACK_UP){
			  userColor = WHITE_UP;
		  }
		  else userColor = BLACK_UP;
	  }
			  
	
	
	public Othello(){
		this.numRows = 8;
		this.numColumns = 8;
		this.currentGameBoardState = new HashMap<Pair<Integer,Integer>, Piece>();
		this.resetBoard();
		this.gameType = "OTHELLO"; // will relate to the client later
		this.turn = false;
	}
	
	public Othello(Othello othello){
		this.numRows = othello.numRows;
		this.numColumns = othello.numColumns;
		this.currentGameBoardState.putAll(othello.currentGameBoardState);
		this.gameType = othello.gameType;
		this.turn = othello.turn;
	}
	
	//sets up the game board by initializing all the spaces and setting the middle spaces to the right color
	@Override
	public void resetBoard() {
		// TODO Auto-generated method stub
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numColumns; j++){
				this.currentGameBoardState.put(new Pair<Integer, Integer>(i,j), new OthelloPiece());
				//temp.put(new Pair<Integer, Integer>(i,j), new TicTacToePiece());
			}
		}
		
		currentGameBoardState.get(new Pair<Integer, Integer>(3,3)).setName(WHITE_UP);
		currentGameBoardState.get(new Pair<Integer, Integer>(3,4)).setName(BLACK_UP);
		currentGameBoardState.get(new Pair<Integer, Integer>(4,4)).setName(WHITE_UP);
		currentGameBoardState.get(new Pair<Integer, Integer>(4,3)).setName(BLACK_UP);
	    
		turn = false;
	}
	

	@Override
	public boolean moveSequence(Pair<Integer, Integer> move, Piece piece, int userToken) {
		// TODO Auto-generated method stub
		return legalMove(move, userToken) && takeTurn(move.getKey(), move.getValue());
	}
	
	//returns bool based on if the location is empty and is the opposite color. doesnt use param piece
	public boolean legalMove(Pair<Integer, Integer> move, int userToken){
		
		boolean result=false;
	    String oppCol=BLACK_UP;
	    if (userToken == 1)
	    {
	      userColor=BLACK_UP;
	      oppCol = WHITE_UP;
	    }else{
	    	userColor=WHITE_UP;
	    	oppCol = BLACK_UP;
	    }

	    int row = move.getKey();
	    int col = move.getValue();
	    
	  //current
	  if (this.turn == true && this.currentGameBoardState.get(move).name.equals(NO_CHIP))
	  {
	    if (row+1<8 && col+1<8 && getPiece(row+1, col+1).equals(oppCol))
	    { 
	    	
	      result=true;
	    }else if(row+1<8 && getPiece(row+1, col).equals(oppCol))
	    {

	    	
	      result=true;
	    }else if(col+1<8 && getPiece(row, col+1).equals(oppCol))
	    {
	    	
	      result=true;
	    }else if (col-1>-1 && getPiece(row, col-1).equals(oppCol))
	    {
	    	
	    	
	      result=true;
	    }else if (row-1>-1 && col-1>-1 && getPiece(row-1, col-1).equals(oppCol))
	    {
	    	
	      result=true;
	    }else if (row-1>-1 && getPiece(row-1, col).equals(oppCol))
	    { 
	    	
	      result=true;
	    }else if(row-1>-1 && col+1<8 && getPiece(row-1, col+1).equals(oppCol))
	    {
	    	
	      result=true;
	    }else if (row+1<8 && col-1>-1 && getPiece(row+1, col-1).equals(oppCol))
	    {
	    	
	      result = true;
	    }
	  }
	  
	  return result;
	}

	//returns 0, 1, or 2 depending on which color has the highest count
	@Override
	public int checkWinner() {
		int countw=0;
	    int countb=0;     
	    for (int i=0; i<8; i++)     
	    { 
	      for (int j=0; j<8; j++)       
	      {       
	        if (getPiece(i,j).equals(BLACK_UP))       
	        {
	          countb++;       
	        }else if (getPiece(i,j).equals(WHITE_UP))       
	        {
	          countw++;       
	        }     
	      }     
	    } if (countb>countw)     
	    {     
	      return 1;    
	    }else if (countw>countb)     
	    {       
	      return 2; 
	    }else     
	    {       
	      return 0;   
	    }
	}

	//returns a bool if the board is full
	public boolean gameOver(){
	    // ADD CODE HERE
	    boolean full=false;
	    int countTot=0;
	    for (int i=0; i<8; i++)
	    {
	      for (int j=0; j<8; j++)
	      {
	        if (getPiece(i, j).equals(BLACK_UP) || getPiece(i, j).equals(WHITE_UP))
	        {
	          countTot++;
	        }
	      }
	    }
        if (countTot==64)
        {
          full=true;
        }
	    return full;
	  }
	
	//returns the color of the tile
	public String getPiece(int i, int j){
		return this.currentGameBoardState.get(new Pair<Integer, Integer>(i,j)).name;
	}
	
	//once a move is made, sets the tile with all middle tiles to that color
	public boolean takeTurn(int row, int col)
	  {
		int count = 0;
		String turn = userColor;
		System.out.println(userColor);
	    // ADD CODE HERE
	    //Check Above
	    //check above & below
	    if (direction(row, col, turn, 0, -1))
	    	count++;
	   if( direction(row, col, turn, 0, 1))
		   count++;
	    //check right & right 
	    if (direction(row, col, turn, 1,0))
	    	count++;
	    if (direction(row, col, turn, -1, 0))
	    	count++;
	    //check corners
	    if (direction(row, col, turn, 1,1))
	    	count++;
	    if (direction(row, col, turn, 1,-1))
	    	count++;
	    if (direction(row, col, turn, -1,1))
	    	count++;
	    if (direction(row, col, turn, -1,-1))
	    	count++;
	    
	    if (count>0)
	    {
			this.currentGameBoardState.get(new Pair<Integer, Integer>(row,col)).name=turn;
			
	    	return true;
	    }
	    return false;
	    
	  }
	//gets a location and color and searches in a specific direction to see if the pieces need to be changed
	  /*This method will check the colours of pegs in an indicated direction
	   *  PRE: 0 <= row < this.gameBoard.getRows() && 0 <= col < this.gameBoard.getColumns()
	   *      colour == BLACK_UP || WHITE_UP
	   *      The row and col values are a valid location for a move in the game.
	   * POST: The pegs of the opposite colour are flipped according to the rules of Othello
	   *       when the method is called and the parameters are entered correctly.
	   */
	  private boolean direction(int row, int column, String colour, int colDir, int rowDir)
	  {
		boolean result = false;
	    int currentRow= row + rowDir + rowDir;
	    int currentCol = column + colDir + colDir;
	    if (currentRow>=8 || currentRow<0 || currentCol>=8 || currentCol<0)
	    {
	      return result;
	    }
	    while (getPiece(currentRow, currentCol).equals(BLACK_UP) || getPiece(currentRow, currentCol).equals(WHITE_UP))
	    {
	      if (getPiece(currentRow, currentCol).equals(colour))
	      {
	        while(!(row==currentRow && column==currentCol))
	        {
	        	this.currentGameBoardState.get(new Pair<Integer, Integer>(currentRow,currentCol)).name = colour;
	          currentRow=currentRow-rowDir;
	          currentCol=currentCol-colDir;
	        }
	        return true;  
	      }else
	      {
	      currentRow=currentRow + rowDir;
	      currentCol=currentCol + colDir;
	      }
	      if (currentRow<0 || currentCol<0 || currentRow==8 || currentCol==8)
	      { 
	        return result;
	      }
	    }
	    return result;
	  }
	  
	  
	
	@Override
	public boolean hasWinner() {
		// TODO Auto-generated method stub
		return gameOver();
	}
 
	@Override
	public GameBoard clone() {
		GameBoard clonedBoard = new Othello();
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
		return gameOver();
	}

	@Override
	public Piece newGamePiece() {
		// TODO Auto-generated method stub
		return new OthelloPiece();
	}



	@Override
	public String getPieceType(int userToken) {
	    if (userToken == 1)
	    {
	      userColor=BLACK_UP;
	    }else{
	    	userColor=WHITE_UP;
	    }
	    return userColor;
	}

}
