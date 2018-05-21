package players;

import util.*;
import board.*;
import javafx.scene.paint.Color;
import javafx.scene.control.ProgressBar;

public interface TimedPlayer {

  public Player getPlayer();
  public ProgressBar getProgressBar();
  public Line makePlay(Board board, Line oppPlay, long timeRemaining);
  public String teamName();
  public String teamMembers();
  public Color getSquareColor();
  public Color getLineColor();
  public int getId();
  public String toString();

  public void restartClock();
  public void stopClock();
  public long timeRemaining();
}
