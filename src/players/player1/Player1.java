// file: Player*.java
// authors: Ricky Yang & Lucas Prosperino
// date: 4/23/2018
//
// purpose: A client which makes moves in dots & boxes. Has two inner classes: ChainLength and potentialChain


package players.player1;

import players.*;
import board.*;
import util.*;

import java.util.*;
import javafx.scene.paint.Color;

public class Player1 implements Player {

	private class potentialChain {

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
					sq=startSq;
					if (side==null || side.equals(null)) {
						return chain;
					}
					else{
						int j = 0;
						boolean b = false;
						//do-while moves through squares
						do{
							Side[] sides = getOpenSides(sq);//works
							j = 0;
							//for loop moves through sides
							for(int i = 0; i<sides.length;i++) {
								//System.out.println("Class: potentialChain - in for-loop - in do-while loop");
								//System.out.println("i = " + i + " : " + sides[i] + " : Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
								//if the side is an open side.
								if (sides[i]==null || sides[i].equals(null)){
									//System.out.println("Getting out of do while loop");
									b=true;
									//System.out.println("Class: potentialChain - b=true -  in for-loop - in do-while loop");
								}
								else {
									//System.out.println("current sq has open sides");
									potentialNextSq=moveToOpen(sq,sides[i],board);//works
									//System.out.println("Potential Next sq = Row " + potentialNextSq.getRow() + " : Col " +potentialNextSq.getCol());
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
												//	System.out.println("NEXT ITEM ADDED");
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
									//System.out.println("boolean b = " + b);
								}
								j++;
							}
							if(b||j==4) {
								//System.out.println("Gettng out of do while loop");
								break;
								//return chain;
							}
						} while(j!=4);
					}
				}
			}
			return chain;
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
				//System.out.println("Moved to North");
			} else if (openSide == Side.SOUTH && inRangeS(row+1) && inRangeS(col)) {
				square = board.getSquare(row+1,col);
				//System.out.println("Moved to South");
			} else if (openSide == Side.WEST && inRangeS(row) && inRangeS(col-1)) {
				square = board.getSquare(row,col-1);
				//System.out.println("Moved to West");
			} else if (openSide == Side.EAST && inRangeS(row) && inRangeS(col+1)) {
				square = board.getSquare(row,col+1);
				//System.out.println("Moved to East");
			} else {
				//System.out.println("Can't move, returning null");
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
	//chains and chainLength are based on squares that are adjacent to each other
	//CONCERN: double counting chains (LOOPS). Two chains can have the same exact set of squares but start at different end points.
	private class ChainLength {
		private ArrayList<Square> chain = null;
		private Square sq= null;
		private Board board;
		private int len = 0;

		public ChainLength () {
			chain = null;
			this.sq=null;
			this.board = null;
			len = 0;
		}

		public ChainLength (Square sq, Board board, Player player) {
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
				//System.out.println("FIRST ITEM ADDED: Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
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
						//System.out.println("SECOND ITEM ADDED: Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
					}
					else {
						return chain;
					}

				}

				int j = 0;
				boolean b = false;

				//do-while moves through squares
				do{
					//System.out.println("START OF THE ADD");
					Side[] sides = getOpenSides(sq);//works
					j = 0;
					//for loop moves through sides
					for(int i = 0; i<sides.length;i++) {
						//System.out.println("i = " + i + " : " + sides[i] + " : Sq = Row " + sq.getRow() + " : Col " + sq.getCol());
						//if the side is an open side.
						if (sides[i]==null || sides[i].equals(null)){
							//System.out.println("Getting out of do while loop");
							return chain;
						}
						else {//need to correct this line
							//System.out.println("current sq has open sides");
							potentialNextSq=moveToOpen(sq,sides[i],board);//works
							//System.out.println("Potential Next sq = Row " + potentialNextSq.getRow() + " : Col " +potentialNextSq.getCol());
							if(potentialNextSq !=null) {
								if(prevSq!=potentialNextSq && sq!=potentialNextSq && potentialNextSq!=null 
										&& !potentialNextSq.hasNMarkedSides(1) && !potentialNextSq.hasNMarkedSides(0) 
										&& inRangeS(potentialNextSq.getRow()) && inRangeS(potentialNextSq.getCol())) {//need to correct this line
									if(potentialNextSq.hasNMarkedSides(2)||potentialNextSq.hasNMarkedSides(3)) {
										prevSq=sq;
										sq = moveToOpen(sq, sides[i],board);//works
										chain.add(sq);
										//System.out.println("NEXT ITEM ADDED");
										break;
									}

								}
							}
						}
						if(i==3) {
							b=true;
							//System.out.println("boolean b = " + b);
						}
						j++;
					}
					if(j==4 || b) {
						//System.out.println("Gettng out of do while loop");
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
				//System.out.println("Moved to North");
			} else if (openSide == Side.SOUTH && inRangeS(row+1) && inRangeS(col)) {
				square = board.getSquare(row+1,col);
				//System.out.println("Moved to South");
			} else if (openSide == Side.WEST && inRangeS(row) && inRangeS(col-1)) {
				square = board.getSquare(row,col-1);
				//System.out.println("Moved to West");
			} else if (openSide == Side.EAST && inRangeS(row) && inRangeS(col+1)) {
				square = board.getSquare(row,col+1);
				//System.out.println("Moved to East");
			} else {
				//System.out.println("Can't move, returning null");
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

	private DBG dbg;
	//    private int countdown;
	//    private Square nextMoveSq;
	//    private Line nextMove;

	public Player1() {
		dbg = new DBG(DBG.PLAYERS, "Player1");
	}

	public Line makePlay(Board board, Line oppPlay, long timeRemaining) {
		//    		System.out.println("Board at start of play");
		//    		//NEED TO RID THIS NEXT print stuff WHEN YOU SUBMIT
		//    		StringBuilder sb = new StringBuilder();
		//    	    for (int row = 0; row < Util.N; row++) {
		//    	      for(int col = 0; col < Util.N; col++) {
		//    	    	  	sb.append("[");
		//    	    	  	if(board.getSquare(row, col).hasNMarkedSides(4))
		//    	    	  		sb.append("*");
		//    	    	  	else
		//    	    	  		sb.append(" ");
		//    	    	  	sb.append("]");
		//    	      }
		//    	      sb.append("\n");
		//    	    }        
		//    	    System.out.println(sb);
		if (board.gameOver())
			return null;


		//GETS SQUARE DATA
		//find the squares with 3 marked sides
		Set<Square> marked3SidesSquares = board.squaresWithMarkedSides(3);

		//find the squares with 3 marked sides
		Set<Square> marked2SidesSquares = board.squaresWithMarkedSides(2);

		//find the squares with 1 marked side
		Set<Square> marked1SideSquares = board.squaresWithMarkedSides(1);

		//find the squares with 0 marked side
		Set<Square> notMarkedSquares = board.squaresWithMarkedSides(0);

		ArrayList<potentialChain> chains = new ArrayList<potentialChain>();

		ArrayList<potentialChain> smallChain = new ArrayList<potentialChain>();
		potentialChain maxPotentialChain = new potentialChain();
		potentialChain minPotentialChain = new potentialChain();


		boolean oneSqAllSurroundingHave2Sides = true;
		if(notMarkedSquares.size()>0) {
			for (Square sq : notMarkedSquares) {
				oneSqAllSurroundingHave2Sides = allSurroundingSqHave2Side(sq, board);
				if(oneSqAllSurroundingHave2Sides==false) {
					break;
				}
			}
		}
		if(oneSqAllSurroundingHave2Sides==false) {
			if(marked1SideSquares.size()>0) {
				for (Square sq : marked1SideSquares) {
					oneSqAllSurroundingHave2Sides = allSurroundingSqHave2Side(sq, board);
					if(oneSqAllSurroundingHave2Sides==false) {
						break;
					}
				}
			}
		}
		//ADDS POTENTIALCHAINS INTO ARRAYLIST
		if(marked3SidesSquares.size()>0 || oneSqAllSurroundingHave2Sides) {
			//System.out.println("Passed 1st condition to count pChains");
			if(oneSqAllSurroundingHave2Sides) {
				//System.out.println("Passed 2nd condition to count pChains");
				Set<Square> marked2SidesSquaresCopy = board.squaresWithMarkedSides(2);
				Iterator<Square> twoSideSq = marked2SidesSquaresCopy.iterator();
				while(twoSideSq.hasNext()) {
					Square sq=twoSideSq.next();
					potentialChain c= new potentialChain(sq,board);
					if(c.getPotentialChain()!=null&&!c.getPotentialChain().equals(null)) {
						System.out.println("added init pChain: " + c.getPotentialChain().toString());
						chains.add(c);
						marked2SidesSquaresCopy.removeAll(c.getPotentialChain());
						twoSideSq=marked2SidesSquaresCopy.iterator();
					}
					else {
						//System.out.println("didn't add pChain");
					}
				}
				minPotentialChain.setSize(100);
				for (potentialChain chain : chains) {

					if(chain.getSize() > maxPotentialChain.getSize()) {
						maxPotentialChain= chain;
					}
					if(chain.getSize() < minPotentialChain.getSize()) {
						minPotentialChain=chain;
					}
				}
			}
		}

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		//		FOR WHEN A SQUARE HAS 3 SIDES
		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//this needs to be more conditional and have more conditions go through
		//get the first square with 3 marked side and mark that open line
		if (!marked3SidesSquares.isEmpty()) {
			//if there are 1 side squares and no side squares still out there, you ALWAYS fill in the 3 side squares
			//except when there is a one sided square covered on all sides by two sided squares
			if(marked1SideSquares.size()>0) {
				boolean allSurroundingHave2Sides = true;
				for (Square sq : marked1SideSquares) {
					allSurroundingHave2Sides = allSurroundingSqHave2Side(sq, board);
				}
				if(notMarkedSquares.size()>0 && !allSurroundingHave2Sides) {
					System.out.println("1 sided sq size = " + marked1SideSquares.size() + " : no sided sq size = " + notMarkedSquares.size());
					if(marked1SideSquares.size()==1) {
						System.out.println("open sqare Row: " + marked1SideSquares.iterator().next().getRow() + " : Col " + marked1SideSquares.iterator().next().getCol());
					}
					Square square = marked3SidesSquares.iterator().next();
					System.out.println("FREE PIECE TAKEN");
					return square.openLines().iterator().next();
				}
			}


			//we assume chainLengths are based on adjacent squares
			ArrayList<ChainLength> chainLengths = new ArrayList<ChainLength> ();
			ChainLength maxChain = new ChainLength();
			//if there is a long chain
			for (Square square : marked3SidesSquares) {
				//System.out.println("TRYING TO MAKE CHAIN");
				chainLengths.add(new ChainLength(square, board, this));
				//System.out.println("MADE CHAIN SUCCESSFULLY");
			}
			//gets max chain
			System.out.println("number of chains = " + chainLengths.size());
			for(ChainLength chain : chainLengths) {
				if(chain.getLen()>maxChain.getLen()) {
					maxChain = chain;
				}
			}
			//if maxChain.size()==# of open squares left on board
			//return maxChain.getSquare.openLines.iterator.next; fill out the square
			int numOpenSquares = marked3SidesSquares.size()+marked2SidesSquares.size()+marked1SideSquares.size()+notMarkedSquares.size();
			if(maxChain.getLen() == numOpenSquares) {
				System.out.println("CHAIN IS REMAINING SQUARES. TAKING ALL OF THEM.");
				return maxChain.getSq().openLines().iterator().next();
			}

			for(potentialChain pC : chains) {
				if(pC.getSize()<=2) {
					smallChain.add(pC);
				}
				System.out.println("added pChain: " + pC.getPotentialChain().toString());;
			}
			System.out.println("small chains size = " + smallChain.size());
			
			//GET LOOPS CODE.
			ArrayList<ChainLength> activeLoops = loopDetector(chainLengths, board);

			System.out.println("Entering Loop Check");
			//tells player to take all other activeChains out there first if there are those out there. Enter IF body if there are NO OTHER ACTiVE CHAINS 
			System.out.println("loops number = " + activeLoops.size() + " : chainLengths size = " + chainLengths.size());
			//the check is <3 since the chainlengths get double counted so we want to only look at this if there are 1 (which is represented as 2 loops) loop active
			if(activeLoops!=null && activeLoops.size()>0 && activeLoops.size()==chainLengths.size()&&activeLoops.size()<3) {
				System.out.println("Loop check says there is 1 active loop");
				//if statement to take all if there are double or single two sided boxes out there
				System.out.println("minPotentialChain: " + minPotentialChain.getSize());
				if(minPotentialChain.getSize()>2) {//if the min potential Chain is >4
					//take squares you can take before making the 2 double boxes
					if(activeLoops.get(1).getLen()==4) {//If loop is size 4, we make 2 double boxes, otherwise it's a situation we don't know how to deal with
						//MIGHT WANT SOME OTHER CONDITION LOOKING FOR POTENTIAL LOOPS, IF THERE'S ONE SIZE>4, THEN WE TAKE THIS ACTIVE LOOP AND ENTER NEXT POTENTIAL LOOP.
						System.out.println("Not Filling Loop: no small chains out there");

						Line tempLine= activeLoops.get(1).getSq().openLines().iterator().next();
						System.out.println("tempLine: "+ tempLine.toString());
						Square tempSq = moveToOpen(activeLoops.get(1).getSq(), tempLine.getSide(), board);
						if(tempSq!=null) {
							System.out.println("tempSq: "+ tempSq.toString());
							for(Line l : tempSq.openLines()) {
								if(l!=tempLine&&!l.equals(tempLine)) {
									System.out.println("Line returned: "+ l.toString());
									return l;
								}
							}
						}
						else {//fill since you failed to make 2 double boxes on the 4sq loop
							System.out.println("Filling Loop in odd way: tried but failed to make 2 double box");
							return activeLoops.get(1).getSq().openLines().iterator().next(); 
						}

					}
					else {//the loop is > 4 sq so you just take since I don't know how to force the loop
						//take correct sq in the loop. THIS IS TEMP CODE
						System.out.println("Filling Loop in odd way: size of active loop > 4");
						return activeLoops.get(1).getSq().openLines().iterator().next();
					}
				}
				else {
					//there are other small chains out there
					//small chain number is optimal
					System.out.println("smallChain size = " + smallChain.size());
					if(smallChain.size()%2==1) {//taking into account small chain number
						//take all in square using primitive data in player 1 class since after we can just force a double box
						System.out.println("Filling Loop in odd way: small chains amount is optimal so we fill");
						return activeLoops.get(1).getSq().openLines().iterator().next();
					}
					//small chain number isn't optimal
					else {
						if(activeLoops.get(1).getLen()==4) {
							System.out.println("Not Filling Loop: small chains aren't optimal and loop is 4 so we make 2 double boxes");

							Line tempLine= activeLoops.get(1).getSq().openLines().iterator().next();
							Square tempSq = moveToOpen(activeLoops.get(1).getSq(), tempLine.getSide(), board);
							if(tempSq!=null) {
								for(Line l : tempSq.openLines()) {
									if(l!=tempLine) {
										return l;
									}
								}
							}
							else {
								return activeLoops.get(1).getSq().openLines().iterator().next();
							}

						}
						else {
							//take correct sq in the loop. THIS IS TEMP CODE
							System.out.println("Filling Loop: there are small chains but size is not optimal but we loop is too big");
							return activeLoops.get(1).getSq().openLines().iterator().next();
						}
					}

				}
			}
			System.out.println("Got past loop check. There are no loops");

			//if statement: we make a method call that recursively returns true or false for the decision we should make.
			// Another helper method is used to return the amount of values in the thing. True being to take all the loop, false to be to force double double boxes.

			System.out.println("MAXCHAIN SIZE = " + maxChain.getLen());
			//covers if there are multiple chains. returns the lines of chains that are smallest first before addressing to big chain to maximize squares covered.
			if(chainLengths.size()>1) {
				for (ChainLength chain : chainLengths) {
					//automatically starts filling smaller than max chains
					if(chain!=maxChain) {
						System.out.println("current chain size = " + chain.getLen() + " : MaxChain size = " + maxChain.getLen());
						return chain.getSq().openLines().iterator().next();
					}
				}
			}


			//covers if maxChain = 2 or 1.
			if(maxChain.getLen()<=2) {
				System.out.println("entering chain<=2 territory");
				//else if maxChain is 2, we know by nature of the chainLength that they chain is adjacent
				if (maxChain.getLen()==2) {
					System.out.println("smallchains: " + smallChain.size());
					if(smallChain.size()%2==1) {//if small chains situation if favorable, we take all
						return maxChain.getSq().openLines().iterator().next();
					}
					Line line = forceDoubleBox(maxChain.getSq(), board);
					if(line == null) {
						System.out.println("CAN'T MAKE DOUBLEBOX, TAKING ALL OF THEM");
						return maxChain.getSq().openLines().iterator().next();
					}
					else {
						System.out.println("MADE DOUBLE BOX!");
						return line;
					}
				}
				else if (maxChain.getLen()==1) {
					//for now we just always take the open box.
					System.out.println("TOOK SINGLE BOX");
					return maxChain.getSq().openLines().iterator().next();
				}
			}
			//Have to see if this takes the boxes in the Chain.
			Square square = marked3SidesSquares.iterator().next();
			System.out.println("JUST TAKING BOXES IN THE CHAIN I GUESS");
			return square.openLines().iterator().next();
		}

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		//		IF THERE ARE SQ's with no or 1 side.
		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		//This code might need to do something where it deals with one side code surrounded by two side code
		//FOR REFERENCE: go look at the mark3sidecode where there is a check for if a square is surrounded by 2 sided squares on all sides        		
		Set<Square> availableSquares = notMarkedSquares;
		availableSquares.addAll(marked1SideSquares);

		System.out.println("Choosing random line from aSq");
		Line line = chooseRandomLine(availableSquares, board);
		if (line != null) {
			System.out.print("randomLine from aSq  = " + line.toString());
			return line;
		}
		//get a line from any of the square that has 1 marked side
		line = chooseRandomLine(marked1SideSquares, board);
		System.out.println("Choosing random line from 1lineSq");
		if (line != null) {
			System.out.print("randomLine from aSq  = " + line.toString());
			return line;
		}

		//------------------------------------------------------------------------------------------------------
		//-----------Choose a Sq with 2 Lines-------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------
		// No choice but have to pick the first line from a square with 2
		// marked sides already.
		// need extra code to determine which one to choose.

		//if smallest potentialChain is 2, we try to force a double box.
		if(minPotentialChain.getSize() == 1) {
			return minPotentialChain.getStartSq().openLines().iterator().next();
		}
		else if(minPotentialChain.getSize()==2) {
			System.out.println("forcing double box");
			return forceDoubleBox(minPotentialChain.getStartSq(), board);
		}
		else if(minPotentialChain.getSize()==3) {
			return minPotentialChain.getStartSq().openLines().iterator().next();
		}
		else if(isALoop(minPotentialChain, board)) {
			return minPotentialChain.getStartSq().openLines().iterator().next();
		}
		potentialChain storeSmallLoop = null;
		for(potentialChain p : chains) {//we go through other chains, to find a loop>4 or the next min chain
			if (isALoop(p,board)) {
				if(storeSmallLoop==null) {
					storeSmallLoop=p;
				}
				else if(p.getSize()<storeSmallLoop.getSize()) {
					storeSmallLoop=p;
				}
			}
		}
		if(storeSmallLoop!=null) {
			return storeSmallLoop.getStartSq().openLines().iterator().next();
		}

		System.out.println("minChain size = " + minPotentialChain.getSize());

		System.out.println("Next Play attempted: " + minPotentialChain.getStartSq().openLines().iterator().next().toString());

		return minPotentialChain.getStartSq().openLines().iterator().next();
	}


	//check the square based on the given line to see if the square
	//has <2 marked side.  Return true if square has <2 marked side
	private boolean doesSideHaveLessThan2SidesMarked(Line line, Board board) {
		Set<Square> attachedSquaresSet = line.getSquares(board);
		Iterator<Square> squareIterator = attachedSquaresSet.iterator();
		while (squareIterator.hasNext()) {
			if (squareIterator.next().openLines().size() <= 2)
				return false;
		}
		return true;
	}

	//given the set of squares, find any open lines of the given square,
	//select the line if it has <2 marked side
	private Line chooseRandomLine(Set<Square> candidates, Board board) {
		if(candidates.isEmpty()) {
			return null;
		}
		List<Square> shuffledCandidates = new ArrayList<Square>(candidates);
		Collections.shuffle(shuffledCandidates);
		for (Square square : shuffledCandidates) {
			ArrayList<Line> openLines = new ArrayList<Line>(square.openLines());
			int r = new Random().nextInt(openLines.size());
			Line line = openLines.get(r);
			while (openLines.size() != 0) {
				if (doesSideHaveLessThan2SidesMarked(line, board)) {
					System.out.print("random line is: ");
					System.out.println(line.toString());
					return line;
				} else {
					System.out.println("chooseRandom: in else statement");
					if(openLines.size()==1) {
						break;
					}
					openLines.remove(r);
					r = new Random().nextInt(openLines.size());
					line = openLines.get(r);
					System.out.println("chooseRandom: reach end of else statement");
				}
			}
		}
		System.out.println("random line is: null");
		return null;
	}

	//returns if squares are adjacent
	public boolean squareAdjacent(Square sq1, Square sq2) {
		if(sq1.getRow()==sq2.getRow()) {
			if(sq1.getCol() == sq2.getCol()+1 || sq1.getCol()==sq2.getCol()-1) {
				System.out.println("squareAdjacent is returning true");
				return true;
			}
		}
		else if(sq1.getCol()==sq2.getCol()) {
			if(sq1.getRow() == sq2.getRow()+1 || sq1.getRow()==sq2.getRow()-1) {
				System.out.println("squareAdjacent is returning true");
				return true;
			}
		}
		System.out.println("squareAdjacent is returning false");
		return false;
	}

	//     find open side (input has 3 sides marked)
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

	private Line forceDoubleBox(Square square, Board board) {
		System.out.println("In forceDoubleBox");
		if (square.hasNMarkedSides(3)) {
			Side openSide = getOpenSide(square);
			Line line1 = new LineC(square.getRow(), square.getCol(), openSide);
			Square nextSquare = moveToOpen(square, openSide, board);

			Iterator<Line> nextOpenLines = nextSquare.openLines().iterator();
			while (nextOpenLines.hasNext()) {
				Line line2 = nextOpenLines.next();
				if (!line1.equals(line2)) {
					//System.out.println("forced double Box" + line2.toString());
					return line2;
				}
			}
		}
		else if (square.hasNMarkedSides(2)) {
			System.out.println("forceDoubleBox: square has 2 marked sides");
			Side[] openSides = getOpenSides(square); // array of 2 lines
			int i = 0;
			Square nextSquare = moveToOpen(square, openSides[i], board);
			System.out.println("forceDoubleBox: movedToOpenSq");    	    		
			if (nextSquare == null || nextSquare.hasNMarkedSides(1)) { // wrong box
				System.out.println("forceDoubleBox: is not a valid sq");    	    		
				i++;
				nextSquare = moveToOpen(square, openSides[i], board);
			}
			System.out.println("forceDoubleBox: is a valid sq");    	    		
			Line line1 = new LineC(square.getRow(), square.getCol(), openSides[i]);
			Iterator<Line> nextOpenLines = nextSquare.openLines().iterator();
			while (nextOpenLines.hasNext()) {
				Line line2 = nextOpenLines.next();
				if (line1.equals(line2)) {
					System.out.println("forced double Box" + line2.toString());
					return line2;
				}
			}
		}
		System.out.println("forced double Box: null");
		return null;
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
	// move to next square in direction of given side
	private Square moveToOpen(Square square, Side openSide, Board board) {
		int row = square.getRow();
		int col = square.getCol();
		if (openSide == Side.NORTH && inRangeS(row-1) && inRangeS(col)) {
			square = board.getSquare(row-1,col);
		} else if (openSide == Side.SOUTH && inRangeS(row+1) && inRangeS(col)) {
			square = board.getSquare(row+1,col);
		} else if (openSide == Side.WEST && inRangeS(row) && inRangeS(col-1)) {
			square = board.getSquare(row,col-1);
		} else if (openSide == Side.EAST && inRangeS(row) && inRangeS(col+1)) {
			square = board.getSquare(row,col+1);
		} else {
			//System.out.println("moveto open returned null");
			return null;
		}
		//System.out.println("moveto open returned a square");
		return square;
	}

	private boolean inRangeS(int i) { return (0 <= i) && (i < 8); }

	//Still needs some work
	private ArrayList<ChainLength> loopDetector(ArrayList<ChainLength> chainLengths, Board board) {
		System.out.println("Chainlength size = " + chainLengths.size());
		ArrayList<ChainLength> copy = new ArrayList<ChainLength>();
		for (ChainLength chain : chainLengths) {
			//if true, could be loop.
			if (chain.getLen() >3) {
				//if true, then you want to remove it. Only what's left after are loops
				if ((chain.getSq().hasNMarkedSides(3) && 
						chain.getChain().get(chain.getLen()-1).hasNMarkedSides(3)))  {
					System.out.println("loopDetector: adding chain since it is a loop.");
					copy.add(chain);
				}
			}
		}
		//potentially might need a nested for loop here to prevent duplicate representations of loops
		return copy;
	}

	private ArrayList<potentialChain> potentialChainsThatArentLoops(ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChains){
		ArrayList<potentialChain> potentialChainsThatArentLoops = new ArrayList<potentialChain>();
		for(potentialChain chain : potentialChains) {
			if(notALoop(loops, chain)) {
				potentialChainsThatArentLoops.add(chain);
			}
		}
		if(potentialChainsThatArentLoops!=null && potentialChainsThatArentLoops.size()>0) {
			System.out.println("there are potential chains that are not loops");
		}
		else {
			System.out.println("there are no potential chains that are not loops");
		}
		return potentialChainsThatArentLoops;
	}
	private int totalSqInPotentialChains(ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChains) {
		int count = 0;
		if(potentialChains!=null) {
			for(potentialChain potentialChain: potentialChains) {
				//if potentialChain is not a loop.
				if(loops==null || notALoop(loops, potentialChain)) {
					count+=potentialChain.getSize();
				}
			} 
		}
		return count;
	}
	private ArrayList<potentialChain> potentialLoopFinder(ArrayList<potentialChain> pchains, Board board) {
		ArrayList<potentialChain> loops = new ArrayList<potentialChain>();
		if(pchains!=null) {
			for (potentialChain pc : pchains) {
				boolean b =true;	
				if(pc.getSize()%2==0) {
					for(Square sq : pc.getPotentialChain()) {
						Square adj1 = null;
						int count = 0;
						for(Line l : sq.openLines()) {
							System.out.println("potentialLoopFinder: moving to open");
							adj1 = moveToOpen(sq,l.getSide(),board);
							if(adj1 == null || adj1.equals(null)) {
								System.out.println("didn't add potential Loop");
								b=false;
								break;
							}
							else if(adj1!=null || !adj1.equals(null)) {
								System.out.println("count++");
								count++;
							}
						}
						if(b==false || count != 2) {
							System.out.println("didn't add potential Loop");
							b=false;
							break;
						}
					}
					if(b) {
						System.out.println("added potential Loop");
						loops.add(pc);
					}
				}
			}
		}
		if(loops!= null) {
			System.out.println("potential loops = " + loops.size());
		}
		else {
			System.out.println("potential loops = null");
		}
		return loops;
	}
	private boolean notALoop(ArrayList<potentialChain> loops, potentialChain potentialChain) {
		for(potentialChain loop : loops) {
			if(potentialChain == loop || potentialChain.equals(loop)) {
				return false;
			}
		}
		return true;
	}

	private boolean isALoop(potentialChain p, Board board) {
		boolean b =true;	
		if(p.getSize()%2==0) {
			for(Square sq : p.getPotentialChain()) {
				Square adj1 = null;
				int count = 0;
				for(Line l : sq.openLines()) {
					System.out.println("potentialLoopFinder: moving to open");
					adj1 = moveToOpen(sq,l.getSide(), board);
					if(adj1 == null || adj1.equals(null)) {
						System.out.println("didn't add potential Loop");
						return false;
						//b=false;
						//break;
					}
					else if(adj1!=null || !adj1.equals(null)) {
						System.out.println("count++");
						count++;
					}
				}
				if(b==false || count != 2) {
					System.out.println("didn't add potential Loop");
					return false;
					//b=false;
					//break;
				}
			}
			if(b) {
				System.out.println("added potential Loop");
				return true;
			}
		}
		return false;
	}

	private boolean allSurroundingSqHave2Side(Square sq, Board board) {
		if(sq.hasNMarkedSides(1)||sq.hasNMarkedSides(0)) {
			Side[] openSides = getOpenSides(sq);
			for(Side side: openSides) {
				Square temp = null;
				if(moveToOpen(sq, side, board)!=null) {
					temp = moveToOpen(sq, side, board);
					if(!temp.hasNMarkedSides(2)&&!temp.hasThreeSides() && !temp.hasFourSides()) {
						System.out.println("Got out of allSurroundingMethod: returning false" + sq.toString());
						return false;
					}
				}
			}
			System.out.println("Got out of allSurroundingMethod: returning true");
			return true;
		}
		System.out.println("Got out of allSurroundingMethod: returning true");
		return true;
	}

	public String teamName() { return "DDD"; }
	public String teamMembers() { return "Ricky Yang and Lucas Prosperino"; }
	public Color getSquareColor() { return Util.PLAYER1_COLOR; }
	public Color getLineColor() { return Color.BLACK; }
	public int getId() { return 1; }
	public String toString() { return teamName(); }
}
