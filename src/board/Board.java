package board;

import java.util.*;
import players.*;
import util.*;

public interface Board {

  // The clone function returns a deep copy of the board. Since the result is
  // a Board, all of the operations specified in this API can be used on the
  // result. A player's makePlay function is given a clone of the board so
  // they can modify their copy of the board without affecting the official
  // board.
  //
  Board clone();

  // Get the square at the specified row and column.
  //
  Square getSquare(int row, int col);

  // The openLines functions returns the set of all open lines.
  // A legal line choice must be an element of this set.
  //
  Set<Line> openLines();

  // The squaresWithMarkedSides function returns the set of all squares
  // with n marked sides. The integer n should be in the range 0..4.
  //
  Set<Square> squaresWithMarkedSides(int n);

  // The markLine function marks a line for a give player and returns the
  // set of 0, 1 or 2 squares claimed. NB that markLine throws an exception
  // if the line is already marked. If markLine is called by player A and
  // an exception is thrown but not handled, then player A loses the game.
  //
  Set<Square> markLine(Player player, Line line);

  // Exports a deep copy of Board to 2D array of squares.
  //
  Square[][] toArray();
  boolean gameOver();
  Score getScore();
  String toString();
  String display();
}
