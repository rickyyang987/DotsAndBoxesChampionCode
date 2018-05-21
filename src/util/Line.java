package util;

import board.*;
import java.util.*;

public interface Line {

  Side getSide();
  int getRow();
  int getCol();

  boolean isLegal(Board board);
  boolean isOpen(Board board);
  Set<Square> getSquares(Board board);

  @Override
  boolean equals(Object other);

  @Override
  int hashCode();

  String toString();
}
