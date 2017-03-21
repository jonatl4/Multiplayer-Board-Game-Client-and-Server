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
public class OthelloPiece extends Piece{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2441877946346381614L;

	
	public OthelloPiece(){
		this.name = " ";
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public OthelloPiece(String name, Pair<Integer, Integer> currentPosition, ArrayList<String> possibleMoves){
		this.name = name;
		this.currentPosition = new Pair<Integer, Integer>(0,0);
		this.possibleMoves = new ArrayList<String>();
	}
	
	public OthelloPiece(Piece othelloPiece){
		this.name = othelloPiece.name;
		this.currentPosition = othelloPiece.currentPosition;
		this.possibleMoves.addAll(othelloPiece.possibleMoves);
	}	
	
	public String getColor(){
		return this.name;
	}
	
	public void setColor(String color){
		this.name = color;
	}
}
