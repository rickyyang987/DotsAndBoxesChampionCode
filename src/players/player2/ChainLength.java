package players.player2;

import players.*;
import board.*;
import util.*;

import java.util.*;

//chains and chainLength are based on squares that are adjacent to each other
//CONCERN: double counting chains (LOOPS). Two chains can have the same exact set of squares but start at different end points.
//WORKS!!!
public class ChainLength {
	private ArrayList<Square> chain = null;
	private Player player = null;
	private Square sq= null;
	private Board board;
	private int len = 0;
	
	public ChainLength () {
		chain = null;
		this.player=null;
		this.sq=null;
		this.board = null;
		len = 0;
	}
	
	public ChainLength (Square sq, Board board, Player player) {
		this.player=player;
		this.sq=sq;
		this.board = board;
		chain = getChainLength();
		len = chain.size();
	}
	
	
	public ArrayList<Square> getChainLength() {
		chain = getChainLength(sq, null);
		return chain;
	}
	
	private ArrayList<Square> getChainLength(Square sq, Square prevSquare) {
		Square potentialNextSq=null;
		Square prevSq = null;
		ArrayList<Square> chain = new ArrayList<Square>();
		if(sq.hasThreeSides()) {
			Side side = getOpenSide(sq);
			chain.add(sq);
//			System.out.println("FIRST ITEM ADDED: Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
			if(moveToOpen(sq,side,board) == null) {
				return chain;
			}
			else {
				Square potSq = moveToOpen(sq,side,board);
				if((potSq.hasNMarkedSides(2)|| potSq.hasNMarkedSides(3))&&(!(potSq.hasNMarkedSides(1)||potSq.hasNMarkedSides(0))
						&& inRangeS(potSq.getRow()) && inRangeS(potSq.getCol()))) {
					prevSq = sq;
					sq = moveToOpen(sq,side,board);
					chain.add(sq);
//					System.out.println("SECOND ITEM ADDED: Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
				}
				else {
					return chain;
				}
				
			}
			
			int j = 0;
			boolean b = false;
			
			//do-while moves through squares
			do{
//				System.out.println("START OF THE ADD");
				Side[] sides = getOpenSides(sq);//works
				j = 0;
				//for loop moves through sides
				for(int i = 0; i<sides.length;i++) {
//					System.out.println("i = " + i + " : " + sides[i] + " : Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
					//if the side is an open side.
					if (sides[i]==null || sides[i].equals(null)){
//						System.out.println("Getting out of do while loop");
						return chain;
					}
					else {//need to correct this line
//						System.out.println("current sq has open sides");
						potentialNextSq=moveToOpen(sq,sides[i],board);//works
//						System.out.println("Potential Next sq = Row " + potentialNextSq.getRow() + " : Col " +potentialNextSq.getCol());
						if(potentialNextSq !=null) {
							if(prevSq!=potentialNextSq && sq!=potentialNextSq && potentialNextSq!=null 
									&& !potentialNextSq.hasNMarkedSides(1) && !potentialNextSq.hasNMarkedSides(0) 
									&& inRangeS(potentialNextSq.getRow()) && inRangeS(potentialNextSq.getCol())) {//need to correct this line
								if(potentialNextSq.hasNMarkedSides(2)||potentialNextSq.hasNMarkedSides(3)) {
									prevSq=sq;
									sq = moveToOpen(sq, sides[i],board);//works
									chain.add(sq);
//									System.out.println("NEXT ITEM ADDED");
									break;
								}
								
							}
						}
					}
					if(i==3) {
						b=true;
//						System.out.println("boolean b = " + b);
					}
					j++;
				}
				if(j==4 || b) {
//					System.out.println("Gettng out of do while loop");
					return chain;
				}
			} while(j!=4);
		}
		return chain;
	}
	private Side getOpenSide(Square square) {
	      if (!square.sideIsMarked(Side.NORTH)) {
	        return Side.NORTH; // N
	      } else if (!square.sideIsMarked(Side.SOUTH)) {
	        return Side.SOUTH; // S
	      } else if (!square.sideIsMarked(Side.WEST)) {
	        return Side.WEST; // W
	      } else {
	        return Side.EAST; // E
	      }
	    }
	// array of all open sides
	 private Side[] getOpenSides(Square square) {
	   Side[] openSides = new Side[4];
	   int i = 0;
	   if (!square.sideIsMarked(Side.NORTH)) {
	     openSides[i] = Side.NORTH; // N
	     i++;
	   } if (!square.sideIsMarked(Side.SOUTH)) {
	     openSides[i] = Side.SOUTH; // S
	     i++;
	   } if (!square.sideIsMarked(Side.WEST)) {
	     openSides[i] = Side.WEST; // W
	     i++;
	   } if (!square.sideIsMarked(Side.EAST)) {
	     openSides[i] = Side.EAST; // N
	   } return openSides;
	 }
	 
	private Square moveToOpen(Square square, Side openSide, Board board) {
 	   int row = square.getRow();
 	   int col = square.getCol();
 	   if (openSide == Side.NORTH && inRangeS(row-1) && inRangeS(col)) {
 	     square = board.getSquare(row-1,col);
// 	     System.out.println("Moved to North");
 	   } else if (openSide == Side.SOUTH && inRangeS(row+1) && inRangeS(col)) {
 	     square = board.getSquare(row+1,col);
// 	     System.out.println("Moved to South");
 	   } else if (openSide == Side.WEST && inRangeS(row) && inRangeS(col-1)) {
 	     square = board.getSquare(row,col-1);
// 	     System.out.println("Moved to West");
 	   } else if (openSide == Side.EAST && inRangeS(row) && inRangeS(col+1)) {
 	     square = board.getSquare(row,col+1);
// 	     System.out.println("Moved to East");
 	   } else {
// 	 	     System.out.println("Can't move, returning null");
 	     return null;
 	   }
 	   return square;
 	 }
	
	 private boolean inRangeS(int i) { return (0 <= i) && (i < 8); }

	//returns if squares are adjacent
    public boolean squareAdjacent(Square sq1, Square sq2) {
    		if(sq1.getRow()==sq2.getRow()) {
    			if(sq1.getCol() == sq2.getCol()+1 || sq1.getCol()==sq2.getCol()-1) {
        			return true;
    			}
    		}
    		else if(sq1.getCol()==sq2.getCol()) {
    			if(sq1.getRow() == sq2.getRow()+1 || sq1.getRow()==sq2.getRow()-1) {
        			return true;
    			}
    		}
    		return false;
    }
    
    //if chainLen>=3
    public boolean ifLongChain() {
    		return len >=3;
    }
    
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Square getSq() {
		return sq;
	}

	public void setSq(Square sq) {
		this.sq = sq;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}
	
	public ArrayList<Square> getChain() {
		return chain;
	}

	public void setChain(ArrayList<Square> chain) {
		this.chain = chain;
	}

}
