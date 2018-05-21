package ui;

import util.Score;
import util.Clock;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressBar;

public interface ControlPanel {

  void setButtonPane(VBox buttonPane);
  void setScorePane(HBox scorePane);

  void setGameScore(Score squares);
  void setMatchScore(Score games);
  //void setProgress(Clock clock);
  int getGames();
  ProgressBar getPlayer1PB();
  ProgressBar getPlayer2PB();
}
