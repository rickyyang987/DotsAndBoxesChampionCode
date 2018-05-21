package board;

import util.*;
import players.*;

import java.util.*;

public interface Square {

  int getRow();
  int getCol();
  Option<Player> getOwner();
  boolean get(Side side);
  void setSide(Side side, boolean value);
  boolean sideIsMarked(Side side);
  boolean hasFourSides();
  boolean hasThreeSides();
  boolean isOwned();
  Set<Line> openLines();
  boolean hasNMarkedSides(int n);
  String toString();
  Square clone();

  void claimFor(Option<Player> p);
  void markLine(Line line, Player player);
}
