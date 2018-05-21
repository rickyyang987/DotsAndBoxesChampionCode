package players.player2;

//import players.*;
import board.*;
import util.*;

import java.util.*;
public class potentialChain {

	private ArrayList<Square> potentialChain;
	private int size;
	
	private Square startSq;
	private Board board;
	
	public potentialChain() {
		board = null;
		size=0;
		startSq=null;
		potentialChain=null;
	}
	
	public potentialChain(Square start, Board board) {
		this.board=board;
		startSq=start;
		potentialChain = getChain();
		if(potentialChain!=null) {
			size = potentialChain.size();
		}
		else {
			size = 0;
		}
	}
	public ArrayList<Square> getChain(){
		potentialChain = getChain(startSq, this.board);
		return potentialChain;
	}
	
	public ArrayList<Square> getChain(Square sq, Board board){
		Square potentialNextSq=null;
		Square prevSq = null;
		Square endSq = null;
		ArrayList<Square> chain = new ArrayList<Square>();
		if(sq.hasNMarkedSides(2)) {
			Side[] startSides = getOpenSides(sq);//works
			chain.add(sq);
			for(Side side : startSides) {
//				if(side != null) {
//					System.out.println("Class: potentialChain: startSq side" + side.name());
//
//				}
				sq=startSq;
//				System.out.println("Class: potentialChain - side = " + side);
				if (side==null || side.equals(null)) {
//					System.out.println("Class: potentialChain - getting out");
					return chain;
				}
				else{
//					System.out.println("Class: potentialChain - passed (side!=null || !side.equals(null))");
					int j = 0;
					boolean b = false;
					//do-while moves through squares
					do{
//						System.out.println("Class: potentialChain - in do-while loop");
						Side[] sides = getOpenSides(sq);//works
						j = 0;
						//for loop moves through sides
						for(int i = 0; i<sides.length;i++) {
//							System.out.println("Class: potentialChain - in for-loop - in do-while loop");
//							System.out.println("i = " + i + " : " + sides[i] + " : Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
							//if the side is an open side.
							if (sides[i]==null || sides[i].equals(null)){
//								System.out.println("Getting out of do while loop");
								b=true;
//								System.out.println("Class: potentialChain - b=true -  in for-loop - in do-while loop");
							}
							else {
//								System.out.println("current sq has open sides");
								potentialNextSq=moveToOpen(sq,sides[i],board);//works
//								System.out.println("Potential Next sq = Row " + potentialNextSq.getRow() + " : Col " +potentialNextSq.getCol());
								if(potentialNextSq !=null) {
									if(potentialNextSq.hasNMarkedSides(3)) {
										System.out.println("Class potentialChain returning null");
										return null;
									}
									else if(prevSq!=potentialNextSq && sq!=potentialNextSq && potentialNextSq!=null 
											&& !potentialNextSq.hasNMarkedSides(1) && !potentialNextSq.hasNMarkedSides(0)
											&& inRangeS(potentialNextSq.getRow()) && inRangeS(potentialNextSq.getCol())
											&& potentialNextSq!=startSq && potentialNextSq != endSq) {//need to correct this line
										if(notInCurrentChain(chain, potentialNextSq)) {
											prevSq=sq;
											sq = potentialNextSq;//works
											endSq=sq;
											chain.add(sq);
//											System.out.println("NEXT ITEM ADDED");
											break;
										}
									}
								}
							}
							if(b) {
								break;
							}
							if(i==3) {
								b=true;
//								System.out.println("boolean b = " + b);
							}
							j++;
						}
						if(b||j==4) {
//							System.out.println("Gettng out of do while loop");
							break;
//							return chain;
						}
					} while(j!=4);
				}
//				else {
//					System.out.println("Class: potentialChain - first loop");
//					return chain;
//				}
			}
		}
		return chain;
	}
	
//	private Side getOpenSide(Square square) {
//	      Iterator<Line> openLines = square.openLines().iterator();
//	      if (!square.sideIsMarked(Side.NORTH)) {
//	        return Side.NORTH; // N
//	      } else if (!square.sideIsMarked(Side.SOUTH)) {
//	        return Side.SOUTH; // S
//	      } else if (!square.sideIsMarked(Side.WEST)) {
//	        return Side.WEST; // W
//	      } else {
//	        return Side.EAST; // E
//	      }
//	    }
	
	// array of all open sides
	 private Side[] getOpenSides(Square square) {
	   Side[] openSides = new Side[4];
	   int i = 0;
//	   Iterator<Line> openLines = square.openLines().iterator();
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
//	     System.out.println("Moved to North");
	   } else if (openSide == Side.SOUTH && inRangeS(row+1) && inRangeS(col)) {
	     square = board.getSquare(row+1,col);
//	     System.out.println("Moved to South");
	   } else if (openSide == Side.WEST && inRangeS(row) && inRangeS(col-1)) {
	     square = board.getSquare(row,col-1);
//	     System.out.println("Moved to West");
	   } else if (openSide == Side.EAST && inRangeS(row) && inRangeS(col+1)) {
	     square = board.getSquare(row,col+1);
//	     System.out.println("Moved to East");
	   } else {
//	 	     System.out.println("Can't move, returning null");
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
  
  private boolean notInCurrentChain(ArrayList<Square> chain, Square sq) {
	  for(Square square : chain) {
		  if (sq == square || sq.equals(square)) {
			  return false;
		  }
	  }
	  return true;
  }
  
//if chainLen>=3
  public boolean ifLongChain() {
  		return size >=3;
  }
  public ArrayList<Square> getPotentialChain() {
		return potentialChain;
	}
	public void setPotentialChain(ArrayList<Square> potentialChain) {
		this.potentialChain = potentialChain;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Square getStartSq() {
		return startSq;
	}
	public void setStartSq(Square startSq) {
		this.startSq = startSq;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}

	
}
