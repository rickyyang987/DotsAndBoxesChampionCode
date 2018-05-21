package controller;
import java.awt.Container;
import ui.*;
import util.*;
import board.*;
import players.*;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public interface Controller {

  ObservableList<Node> getChildren();
  Display getDisplay();
  void setBoardDisplay(BoardDisplay bd);
  ControlDisplay getControlDisplay();
  //BoardDisplay getBoardDisplay();
  void setControlDisplay(ControlDisplay cd);
  void setBoard(Board board);
  Board getBoard();
  void modeReset(Mode mode);
  Mode getMode();
  Player getPlayer(int i);
  void setPace(double sliderPace);
  long getTimeout();

  // This one comes from the UI as the result of mouse click.
  //
  void makeHumanPlay(Line line);

  // This routine is called by the button actionListener in
  // the UI. This routine should confirm with the board that
  // the play is playable given the board
  //
  //    void makePlay(Player player, int row, int col, Side side);

  // This routine reinitializes the board. If the radio button
  // is selected, it sets the controller into Interactive Mode.
  // This creates one player which will be called on to make a
  // play in response to the step button. Other buttons are
  // deactivated.
  //
  void stepResponder();
  void stopResponder();
  //    Score playOneGame(Score overAll);
  void playOneGame();
  void playMatch();
  //void setContentPane(Container pane);
}
