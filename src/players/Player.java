

package players;

import util.*;
import board.*;
import javafx.scene.paint.Color;

public interface Player {

  // makePlay accepts a board, the oppenent's play and a long integer
  // representing the time remaining in milliseconds. The makePlay function
  // will be called by the main app. It must return a Line. If it throws
  // an exception or otherwise fails to return a valid Line within the
  // allotted time, the player loses the game.
  //
  public Line makePlay(Board board, Line oppPlay, long timeRemaining);
  public String teamName();
  public String teamMembers();
  public Color getSquareColor();
  public Color getLineColor();
  public int getId();
  public String toString();
}
