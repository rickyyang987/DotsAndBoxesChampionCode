package players;

import util.*;
import board.*;
import javafx.scene.paint.Color;
import javafx.scene.control.ProgressBar;

public class TimedPlayerC implements TimedPlayer {

  private Player p;
  private Clock clock;
  private ProgressBar pb;

  public TimedPlayerC(Player p, long ms, ProgressBar pb) {
    this.p = p;
    this.clock = new Clock(p, ms);
    this.pb = pb;
  }

  public Player getPlayer () { return this.p; }
  public ProgressBar getProgressBar () { return this.pb; }
  public Line makePlay(Board board, Line oppPlay, long timeRemaining) {
    return p.makePlay(board, oppPlay, timeRemaining);
  }
  public String teamName() { return p.teamName(); }
  public Color getSquareColor() { return p.getSquareColor(); }
  public Color getLineColor() { return p.getLineColor(); }
  public String teamMembers() { return p.teamMembers(); }
  public int getId() { return p.getId(); }
  public String toString() { return p.toString(); }

  public void restartClock() { clock.restart(); }
  public void stopClock() { clock.stop(); }
  public long timeRemaining() { return clock.timeRemaining(); }
}
