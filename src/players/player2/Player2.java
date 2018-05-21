// file: Player*.java
// authors: Ricky Yang & Lucas Prosperino
// date: 4/23/2018
//
// purpose: A client which makes moves in dots & boxes.


package players.player2;

import players.*;
import board.*;
import util.*;

import java.util.*;
import java.util.List;

import javafx.scene.paint.Color;
import java.awt.*;

public class Player2 implements Player {

    private DBG dbg;
    private int countdown;
    private Square nextMoveSq;
    private Line nextMove;

    public Player2() {
        dbg = new DBG(DBG.PLAYERS, "Player1");
    }

    public Line makePlay(Board board, Line oppPlay, long timeRemaining) {
    		System.out.println("Board at start of play");
    		//NEED TO RID THIS NEXT print stuff WHEN YOU SUBMIT
    		StringBuilder sb = new StringBuilder();
    	    for (int row = 0; row < Util.N; row++) {
    	      for(int col = 0; col < Util.N; col++) {
    	    	  	sb.append("[");
    	    	  	if(board.getSquare(row, col).hasNMarkedSides(4))
    	    	  		sb.append("*");
    	    	  	else
    	    	  		sb.append(" ");
    	    	  	sb.append("]");
    	      }
    	      sb.append("\n");
    	    }        
    	    System.out.println(sb);
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
//    			System.out.println("Passed 1st condition to count pChains");
	        		
	    		if(oneSqAllSurroundingHave2Sides) {
//	    			System.out.println("Passed 2nd condition to count pChains");
//	    			boolean check = false;
                Set<Square> marked2SidesSquaresCopy = board.squaresWithMarkedSides(2);
                Iterator<Square> twoSideSq = marked2SidesSquaresCopy.iterator();
                while(twoSideSq.hasNext()) {
                		Square sq=twoSideSq.next();
                		potentialChain c= new potentialChain(sq,board);
                		if(c.getPotentialChain()!=null&&!c.getPotentialChain().equals(null)) {
    	            			System.out.println("added init pChain: " + c.getChain().toString());
	    	            		chains.add(c);
    	            			marked2SidesSquaresCopy.removeAll(c.getPotentialChain());
//	    	            		for(Square sq1 : c.getPotentialChain()) {
//	    	            			marked2SidesSquaresCopy.remove(sq1);
//	    	            		}
	    	            		twoSideSq=marked2SidesSquaresCopy.iterator();
	                		}
                		else {
//    	            		System.out.println("didn't add pChain");
                		}
                }
//	            
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
//        	
        }
        
        

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		FOR WHEN A SQUARE HAS 3 SIDES
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //this needs to be more conditional and have more conditions go through
        //get the first square with 3 marked side and mark that open line
        if (!marked3SidesSquares.isEmpty()) {
        		//if there are 1 side squares and no side squares still out there, you ALWAYS fill in the 3 side squares
        			//except when there is a one sided square covered on all sides by two sided squares
        		//SOMETHING MIGHT BE WRONG HERE
        		if(marked1SideSquares.size()>0) {
        			boolean allSurroundingHave2Sides = true;
//        			int count=0;
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
        		//this need to eventually be fixed to make it where it will choose 
        		for (Square square : marked3SidesSquares) {
//                System.out.println("TRYING TO MAKE CHAIN");
                chainLengths.add(new ChainLength(square, board, this));
//                System.out.println("MADE CHAIN SUCCESSFULLY");
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
        		//DEBUGGING CODE
        		for(ChainLength c : chainLengths) {
        			System.out.println("added Chain: " + c.getChain().toString());
        		}
        		
        		for(potentialChain pC : chains) {
        			if(pC.getSize()<=2) {
            			smallChain.add(pC);
            		}
            		System.out.println("added pChain: " + pC.getChain().toString());;
        		}
        		System.out.println("small chains size = " + smallChain.size());

        		
        		//all chains at this point have been added

        		
//        		//GET LOOPS CODE.
        		ArrayList<ChainLength> activeLoops = loopDetector(chainLengths, board);
        		if(this.countdown>0) {
        			this.nextMove = fillCycle(nextMoveSq, board);
            		return this.nextMove;
            }
        		
        		System.out.println("Entering Loop Check");
        		//tells player to take all other activeChains out there first if there are those out there. Enter IF body if there are NO OTHER ACTiVE CHAINS 
        		System.out.println("loops number = " + activeLoops.size() + " : chainLengths size = " + chainLengths.size());
        		//the check is <3 since the chainlengths get double counted so we want to only look at this if there are 1 (which is represented as 2 loops) loop active
        		if(activeLoops.size()==chainLengths.size()&&activeLoops.size()<3) {
            		System.out.println("Loop check says there is 1 active loop");
        			//if statement to take all if there are double or single two sided boxes out there
            		System.out.println("minPotentialChain: " + minPotentialChain.getSize());
        			if(minPotentialChain.getSize()>2) {
//        				if(takeOrNot(activeLoops.get(1), potentialLoopFinder(chains, board), chains)) {
//                    		System.out.println("Filling Loop");
////        					countdown = activeLoops.get(1).getLen()-1;
////        					this.nextMove=fillCycle(activeLoops.get(1).getSq(), board);
////        					return nextMove;
//                    		return activeLoops.get(1).getSq().openLines().iterator().next();
//        				}
//        				else {
        					//take squares you can take before making the 2 double boxes
        					if(activeLoops.get(1).getLen()==4) {
	                    		System.out.println("Not Filling Loop");

        						Line tempLine= activeLoops.get(1).getSq().openLines().iterator().next();
        						Square tempSq = moveToOpen(activeLoops.get(1).getSq(), tempLine.getSide(), board);
        						if(tempSq!=null) {
        							for(Line l : tempSq.openLines()) {
            							if(l!=tempLine) {
            								return l;
            							}
            						}
        						}
        						else {//fill since you failed to make 2 double boxes
            						return activeLoops.get(1).getSq().openLines().iterator().next(); 
        						}
        						
        					}
        					else {//the loop is > 4 sq so you just take since I don't know how to force the loop
        						//take correct sq in the loop. THIS IS TEMP CODE
//                            		System.out.println("Filling Loop");
//        							countdown = activeLoops.get(1).getLen()-1;
//                					this.nextMove=fillCycle(activeLoops.get(1).getSq(), board);
//                					return nextMove;
                        		System.out.println("Filling Loop in odd way");
        						return activeLoops.get(1).getSq().openLines().iterator().next();
        					}
//        				}
        				//return fcn(that returns an int) > fcn(that returns int
        			}
        			else {
        				//small chain number is optimal
        				System.out.println("smallChain size = " + smallChain.size());
        				if(smallChain.size()%2==1) {
        					//take all in square using primitive data in player 1 class since after we can just force a double box
//                    		System.out.println("Filling Loop");
//            				this.countdown = activeLoops.get(1).getLen()-1;
//            				return fillCycle(activeLoops.get(1).getSq(), board);
                    		System.out.println("Filling Loop in odd way");
        					return activeLoops.get(1).getSq().openLines().iterator().next();
        				}
        				//small chain number isn't optimal
        				else {
        					if(activeLoops.get(1).getLen()==4) {
	                    		System.out.println("Not Filling Loop");

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
//                            		System.out.println("Filling Loop");
//        							countdown = activeLoops.get(1).getLen()-1;
//                					this.nextMove=fillCycle(activeLoops.get(1).getSq(), board);
//                					return nextMove;
                        		System.out.println("Filling Loop in odd way");
        						return activeLoops.get(1).getSq().openLines().iterator().next();
        					}
        				}
        				
        			}
        		}
        		System.out.println("Got past loop check. There are no loops");

        			//if statement: we make a method call that recursively returns true or false for the decision we should make.
        			// Another helper method is used to return the amount of values in the thing. True being to take all the loop, false to be to force double double boxes.
        		
        		System.out.println("MAXCHAIN SIZE = " + maxChain.getLen());
        		//DOESN"T WORK properly
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
        				//LUCKYCODE
        				if(smallChain.size()%2==1) {
        					return maxChain.getSq().openLines().iterator().next();
//        					if(minPotentialChain.getSize()==1) {
//                    			System.out.println("there's potential chain = 1");
//            				}
//            				if(minPotentialChain.getSize()==2) {
//                    			System.out.println("there's potential chain = 2");
//            					return maxChain.getSq().openLines().iterator().next();
//            				}
        				}
        				//NEEDS UPDATE: If there's already an odd number of (potential double boxes + potential forced singles), 
        							//we need to take all squares in that chain
        					//This will likely be done after we do the code that can make this happen
        				Line line = forceDoubleBox(maxChain.getSq(), board);
        				if(line == null) {
        					System.out.println("CAN'T MAKE DOUBLEBOX, TAKING ALL OF THEM");
        					return maxChain.getSq().openLines().iterator().next();
        				}
        				else {
        					System.out.println("MADE DOUBLE BOX!");
        					return line;
        				}
        				//call function potentialDoubleBox(sq1, sq2)
    						//If returns true call the makeDoubleBox fcn
        				//Else it will just have to fill both boxes
        			}
        			else if (maxChain.getLen()==1) {
        				//POTENTIAL for not filling out the square and instead making a double box somewhere else
        				//for now we just always take the open box.
        				System.out.println("TOOK SINGLE BOX");
        				return maxChain.getSq().openLines().iterator().next();
        			}
        		}
        		//Have to see if this takes the boxes in the Chain.
            Square square = marked3SidesSquares.iterator().next();
            System.out.println("JUST TAKING BOXES IN THE CHAIN I GUESS");
            return square.openLines().iterator().next();
            //if chain is not long
            //makeDoubleBox
        }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		IF THERE ARE SQ's with no or 1 side.
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //This code might need to do something where it deals with one side code surrounded by two side code
        		//FOR REFERENCE: go look at the mark3sidecode where there is a check for if a square is surrounded by 2 sided squares on all sides

        //this cannot be random. Need some way of determining this in a way to make it optimal for chains
        //get a line from any of the square that has no marked side
        		
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
        //NEEDS FIXING: GIVES EXCEPTIONS SOMETIMES
        // No choice but have to pick the first line from a square with 2
        // marked sides already.
        //need extra code to determine which one to choose.
//        Line nextLine = getSmallestChainLength(board);
//        return nextLine;
        
        //if smallest potentialChain is 2, we try to force a double box.
        if(minPotentialChain.getSize() == 1) {
        		return minPotentialChain.getStartSq().openLines().iterator().next();
        }
        else if(minPotentialChain.getSize()==2) {
        		System.out.println("forcing double box");
        		return forceDoubleBox(minPotentialChain.getStartSq(), board);
        }
        
//        ArrayList<potentialChain> a = potentialLoopFinder(chains, board);
//        if (a.size() >0) {
//        		return a.get(1).getStartSq().openLines().iterator().next();
//        }
        
        System.out.println("minChain size = " + minPotentialChain.getSize());
//        for(Square sq : minChain.getChain()) {
//        		System.out.println("minChain sq: " + sq.toString());
//        }
        System.out.println("Next Play attempted: " + minPotentialChain.getStartSq().openLines().iterator().next().toString());

        return minPotentialChain.getStartSq().openLines().iterator().next();
        
//        Set<Line> lines = board.openLines();
//        List<Line> shuffledLines = new ArrayList<Line>(lines);
        //need something here to make it 3 sides on a one square or a 2 square chain.
        //or do the simulated games for this.
//        Collections.shuffle(shuffledLines);
//        return shuffledLines.get(0);
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
//    	    	   	System.out.println("forced double Box" + line2.toString());
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
		System.out.println("moveto open returned null");
    	     return null;
    	   }
   		System.out.println("moveto open returned a square");
    	   return square;
    	 }
    	 
    	 private boolean inRangeS(int i) { return (0 <= i) && (i < 8); }
    	 
    	 //Still needs some work
    	 private ArrayList<ChainLength> loopDetector(ArrayList<ChainLength> chainLengths, Board board) {
    		 System.out.println("Chainlength size = " + chainLengths.size());
    		 ArrayList<ChainLength> copy = new ArrayList<ChainLength>(chainLengths);
    			for (ChainLength chain : chainLengths) {
    				//if true, could be loop.
    				if (chain.getLen() % 2 == 0 && chain.getLen() < 17 && chain.getLen() != 2) {
    					//if true, then you want to remove it. Only what's left after are loops
    					if (!squareAdjacent(chain.getChain().get(chain.getLen()-1), (chain.getSq())) ||
	    	            (!(chain.getSq().hasNMarkedSides(3) && 
	    	            chain.getChain().get(chain.getLen()-1).hasNMarkedSides(3))))  {
    	    					System.out.println("loopDetector: removing chain from since it isn't a loop.");
	    					copy.remove(chain);
    					}
    				} else {
    					System.out.println("loopDetector: removing chain from since it isn't a loop.");
    					copy.remove(chain);
    				}
    			}
    			//potentially might need a nested for loop here to prevent duplicate representations of loops
    			return copy;
    	  }
    	 //return true if you take.
    	 private boolean takeOrNot(ChainLength currentLoop, ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChains) {
		if(loops == null||loops.size() == 0) {
			System.out.println("there's no other loop out there");
			//returns true if should take
			return ((currentLoop.getLen()+2*(potentialChainsThatArentLoops(loops,potentialChains).size())) 
					> 
			(totalSqInPotentialChains(loops,potentialChains)-2*(potentialChainsThatArentLoops(loops,potentialChains).size())+currentLoop.getLen()-4));
		}
		else if(loops.size()>0) {
			System.out.println("there are other loops out there");
			return possibleSituation(false, loops.iterator().next(), loops, potentialChainsThatArentLoops(loops, potentialChains)) > possibleSituation(true, loops.iterator().next(), loops, potentialChainsThatArentLoops(loops, potentialChains));
			//take>not take
//			return (currentLoop.getSize()+takeOrNot());
		}
    		 return false;
    		 
    	 }
    	 
    	 private int oneLoopSituation(boolean take, potentialChain loop, ArrayList<potentialChain> potentialChains) {
    		 ArrayList<potentialChain> loops = new ArrayList<potentialChain>();
    		 if(loop!=null) {
        		 loops.add(loop);
    		 }
    		 if(take) {
    			 if(loop!=null) {
        			 return (loop.getSize()+2*(potentialChainsThatArentLoops(loops,potentialChains).size()));
    			 }
    			 else {
    				 return 0;
    			 }
    		 }
    		 else {
    			 if(loop!=null) {
        			 return (totalSqInPotentialChains(loops,potentialChains)-2*(potentialChainsThatArentLoops(loops,potentialChains).size())+loop.getSize()-4);
    			 }
    			 else {
    				 return 0;
    			 }
    		 }
    	 }
    	 
    	 private int possibleSituation(boolean nextLoopYours, potentialChain currentLoop, ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChainsWithNoLoops) {
    		 //need to remove the loop already checked from the loops list
    		 if(loops == null || loops.size()==0) {
    			 if(nextLoopYours) {
    				 return isGreater(oneLoopSituation(true, currentLoop, potentialChainsWithNoLoops), oneLoopSituation(false, currentLoop, potentialChainsWithNoLoops));
    			 }
    		 }
    		 potentialChain c = loops.remove(1);

    		 System.out.println("loops size after removal = " + loops.size());
    		 if(nextLoopYours) {
    			 return isGreater(currentLoop.getSize()+possibleSituation(false, c, loops, potentialChainsWithNoLoops), possibleSituation(true, c, loops, potentialChainsWithNoLoops));
    		 }
    		 else {
    			 return isSmaller(possibleSituation(false, c, loops, potentialChainsWithNoLoops), currentLoop.getSize()+possibleSituation(true, c, loops, potentialChainsWithNoLoops));
    		 }
    	 }
//    	 private int possibleSituation(boolean nextLoopYours, boolean take, potentialChain currentLoop, ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChains) {
//    		 if(nextLoopYours) {
//    			 
//    		 }
//    		 return 0;
//    	 }
    	//return true if you take.
//    	 private boolean takeOrNot(ArrayList<potentialChain> loops, ArrayList<potentialChain> potentialChains) {
//		if(loops.size() == 1) {
//			//returns true if should take
//			return ((loops.get(1).getSize()+2*(potentialChainsThatArentLoops(loops,potentialChains).size())) 
//					> 
//			(totalSqInPotentialChains(loops,potentialChains)-2*(potentialChainsThatArentLoops(loops,potentialChains).size())+loops.get(1).getSize()-4));
//		}
//		else if(loops.size()>1) {
////			return (loops.get(1).getSize()+takeOrNot());
//		}
//    		 return false;
//    		 
//    	 }
    	 
//    	 private int potentialSquaresWon(Boolean b) {
//    		 return 0;
//    	 }
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
    		 for(potentialChain potentialChain: potentialChains) {
    			 //if potentialChain is not a loop.
    			 if(loops==null || notALoop(loops, potentialChain)) {
    				 count+=potentialChain.getSize();
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
     		    			for(Square sq : pc.getChain()) {
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
    		
    	 private int isGreater(int a, int b) {
    		 if(a>=b) {
    			 return a;
    		 }
    		 else return b;
    	 }
    	 private int isSmaller(int a, int b) {
    		 if(a<=b) {
    			 return a;
    		 }
    		 else return b;
    	 }
    	 
    	 private Line fillCycle(Square nextSq, Board board) {
    		 
    		 if(countdown == 0) {
    			 return null;
    		 }
    		 else {
    			 System.out.println("countdown = " + countdown);
    			 countdown --;
    			 nextMove=nextSq.openLines().iterator().next();
    			 nextMoveSq = moveToOpen(nextMoveSq,nextMoveSq.openLines().iterator().next().getSide(), board);
    			 return nextMove;
    		 }
    	 }
    	 
    	 private Line chooseRandomLine1(Set<Square> candidates, Board board) {
    	        List<Square> shuffledCandidates = new ArrayList<Square>(candidates);
    	        Collections.shuffle(shuffledCandidates);
    	        for (Square square : shuffledCandidates) {
    	            Iterator<Line> openLines = square.openLines().iterator();
    	            while (openLines.hasNext()) {
    	                Line line = openLines.next();
    	                if (doesSideHaveLessThan2SidesMarked(line, board))
    	                    return line;
    	            }
    	        }
    	        return null;
    	    }
    	 private boolean allSurroundingSqHave2Side(Square sq, Board board) {
    		 if(sq.hasNMarkedSides(1)||sq.hasNMarkedSides(0)) {
    			 Side[] openSides = getOpenSides(sq);
// 				Set<Line> openLines = sq.openLines();
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
    	 
    	 public String teamName() { return "Player 2"; }
    	  public String teamMembers() { return "Player 2"; }
    	  public Color getSquareColor() { return Color.MAROON; }
    	  public Color getLineColor() { return Color.MAROON; }
    	  public int getId() { return 2; }
    	  public String toString() { return teamName(); }
}
