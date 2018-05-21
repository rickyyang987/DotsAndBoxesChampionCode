package board;

import util.*;
import players.*;
import players.player1.*;
import players.player2.*;
import controller.*;

import java.util.*;
import javafx.scene.paint.Color;
import ui.BoardDisplay;

public class BoardC implements Board {

  private DBG dbg;
  private Controller controller;
  private Square[][] board;
  private Color shade = Util.ONE_LINE_TO_GO;
  private BoardDisplay boardDisplay;

  private static final String badIndex =
    "bad index row=%d, col=%d, you lose.";
  private static final String alreadyMarked =
    "board[%d][%d].%s already marked, you lose.";

  public BoardC(Controller controller, int rows, int cols) {
    this.controller = controller;

    this.dbg = new DBG(DBG.BOARD, "BoardC");

    this.board = new Square[Util.N][Util.N];

    for(int row = 0; row < Util.N; row++)
      for(int col = 0; col < Util.N; col++)
        this.board[row][col] = new SquareC(controller, row, col);
  }

  public BoardC(Controller controller, Square[][] squares) {
    this.controller = controller;
    this.board = squares;
  }

  public Score getScore() {
    Score score = new ScoreC(0, 0);
    for(int row = 0; row < Util.N; row++)
      for(int col = 0; col < Util.N; col++) {
        Square sq = board[row][col];
        Option<Player> op = sq.getOwner();
        if(op.hasValue())
          score.add(op.valueOf(), 1);
      }
    return score;
  }

  private boolean inRange(int i) { return (0 <= i) && (i < Util.N); }

  public Square getSquare(int row, int col) {
    if (inRange(row) && inRange(col))
      return this.board[row][col];
    else {
      String msg = String.format(badIndex, row, col);
      throw new RuntimeException("getSquare: " + msg);
    }
  }

  // The following markLine function is one of the main entry points
  // for the game. It handles the logic of marking a line for a player
  // as well as making the UI calls to color it and it takes care of
  // the logic of any potentially claimed squares. In particular, it
  // returns a set of 0, 1 or 2 squares claimed by marking the line.
  // The markLine function is called from ControllerC.java.
  //
  public Set<Square> markLine(Player player, Line line) {
    this.boardDisplay = controller.getDisplay().getBoardDisplay();

//    System.out.format("markLine: line = %s.%n", line);
    int row = line.getRow(),
        col = line.getCol();
    Side side = line.getSide();

    Square
      square = this.getSquare(row, col),
      above, below, right, left;

    if(dbg.debug)
      dbg.println("markLine: working on square=" + square.toString());

    Set<Square> claimedSquares = new HashSet<Square>();
    boolean completedASquare;

    // Check to ensure they aren't marking something that's already
    // marked.
    //
    if (square.sideIsMarked(side)) {
      String msg = String.format(alreadyMarked, row, col, side);
      throw new RuntimeException("markLine: " + msg);
    }
    else {
      square.markLine(line, player);

      completedASquare = square.hasFourSides();

      Option<Player> thisPlayer = new Some<Player>(player);
      if (completedASquare) {
        square.claimFor(thisPlayer);
        claimedSquares.add(square);
        if(dbg.debug)
          dbg.println("claiming center" + square);
      }
      else
        if (square.hasThreeSides()) boardDisplay.colorTile(square, shade);

      if (side == Side.NORTH && row > 0) {
        above = this.getSquare(row - 1, col);
        above.setSide(Side.SOUTH, true);
        if(above.hasFourSides()) {
          above.claimFor(thisPlayer);
          claimedSquares.add(above);
          if(dbg.debug)
            dbg.println("claiming northerly" + above);
        }
        else
          if (above.hasThreeSides()) boardDisplay.colorTile(above, shade);
      }
      else if (side == Side.SOUTH && row < (Util.N - 1)) {
        below = this.getSquare(row + 1, col);
        below.setSide(Side.NORTH, true);
        if(below.hasFourSides()) {
          below.claimFor(thisPlayer);
          claimedSquares.add(below);
          if(dbg.debug)
            dbg.println("claiming southerly" + below);
        }
        else
          if (below.hasThreeSides()) boardDisplay.colorTile(below, shade);
      }
      else if (side == Side.EAST && col < (Util.N - 1)) {
        right = this.getSquare(row, col + 1);
        right.setSide(Side.WEST, true);
        if(right.hasFourSides()) {
          right.claimFor(thisPlayer);
          claimedSquares.add(right);
          if(dbg.debug)
            dbg.println("claiming easterly" + right);
        }
        else
          if (right.hasThreeSides()) boardDisplay.colorTile(right, shade);
      }
      else if (side == Side.WEST && col > 0) {
        left = this.getSquare(row, col - 1);
        left.setSide(Side.EAST, true);
        if(left.hasFourSides()) {
          left.claimFor(thisPlayer);
          claimedSquares.add(left);
          if(dbg.debug)
            dbg.println("claiming westerly" + left);
        }
        else
          if (left.hasThreeSides()) boardDisplay.colorTile(left, shade);
      }
      return claimedSquares;
    }
  }

  private boolean isFull() {
    int
      p1s = this.getScore().getPlayer1(),
      p2s = this.getScore().getPlayer2();
    return (p1s + p2s) == Util.N * Util.N;
  }

  public boolean gameOver() { return this.isFull(); }

  public Set<Line> openLines() {
    Set<Line> lineSet = new HashSet<Line>();
    // NB: The Line type has a reasonable equivalence relation
    //     that equates e.g., [0][0].EAST with [0][1].WEST so
    //     no worries RE redundant lines in this set.
    for(int row = 0; row < Util.N; row++)
      for(int col = 0; col < Util.N; col++) {
        Set<Line> sqLineSet = this.getSquare(row, col).openLines();
        if(dbg.debug)
          dbg.println("openLines: adding group " + sqLineSet);
        lineSet.addAll(sqLineSet);
      }
    return lineSet;
  }

  public Set<Square> squaresWithMarkedSides(int n) {

    Set<Square> theSet = new HashSet<Square>();
    for(int row = 0; row < Util.N; row++)
      for(int col = 0; col < Util.N; col++) {
        Square square = getSquare(row, col);
        if (square.hasNMarkedSides(n))
          theSet.add(square);
      }
    return theSet;
  }

  // export returns a 2D array containing -clones- of the Squares in
  // the board.
  //
  public Square[][] toArray() {

    Square[][] out = new Square[Util.N][Util.N];
    for(int row = 0; row < Util.N; row++) {
      for(int col = 0; col < Util.N; col++)
        out[row][col] = this.getSquare(row, col).clone();
    }
    return out;
  }

  @Override
  public Board clone() {
    Square[][] squares = this.toArray();
    return new BoardC(this.controller, squares);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (int row = 0; row < Util.N; row++) {
      sb.append("[");
      for(int col = 0; col < Util.N; col++)
        sb.append(this.getSquare(row, col).toString() + ", ");
      sb.append("],\n");
    }
    return sb.toString() + "]";
  }
  public String display() {
	    StringBuilder sb = new StringBuilder("[");
	    for (int row = 0; row < Util.N; row++) {
//	      sb.append("[");
	      for(int col = 0; col < Util.N; col++) {
	    	  	sb.append("[");
	    	  	if(board[row][col].isOwned())
	    	  		sb.append("*");
//		       sb.append(this.getSquare(row, col).toString() + ", ");
	    	  	else
	    	  		sb.append(" ");
	    	  	sb.append("]");
	      }
//	      sb.append("],\n");
	    }
	    return sb.toString() + "]";
	}
}
