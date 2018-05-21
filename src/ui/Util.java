package ui;

import util.*;
import players.*;
import controller.*;
//import javax.swing.*;
//import java.awt.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;


public class Util {

  private Controller controller;
  private DBG dbg;

  public Util(Controller controller) {
    this.controller = controller;
    this.dbg = new DBG(DBG.UI, "Util");
  }

  public ControlPanel makeControlPanel(Controller controller, Mode mode) {

    if(dbg.debug)
      dbg.println("makeControlPanel: making a " + mode + " control panel.");

    switch(mode) {
    case INTERACTIVE : return new InteractiveControlPanel(controller);
    case GAME        : return new GameControlPanel(controller);
    case MATCH       : return new MatchControlPanel(controller);
    default          : return new InteractiveControlPanel(controller);
    }
  }

  // A score panel for an INTERACTIVE game.
  //
  public HBox makeScorePane(TextField p1sqc, TextField p2sqc) {
    Player
      player1 = controller.getPlayer(1),
      player2 = controller.getPlayer(2);

    HBox scorePane = new HBox();
    scorePane.setSpacing(10);
    Pane p1 = makePlayerPaneEntry(player1, p1sqc);
    Pane p2 = makePlayerPaneEntry(player2, p2sqc);
    scorePane.getChildren().add(p1);
    scorePane.getChildren().add(p2);
    return scorePane;
  }

  // A score panel for a GAME or MATCH.
  //
  public HBox makeScorePane(TextField p1sqc, TextField t1GCount, ProgressBar pb1,
                            TextField p2sqc, TextField t2GCount, ProgressBar pb2) {
    Player
      player1 = controller.getPlayer(1),
      player2 = controller.getPlayer(2);

    HBox scorePane = new HBox();
    scorePane.setSpacing(10);
    scorePane.setAlignment(Pos.CENTER);

    //scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
    VBox p1 = makePlayerPaneEntry(player1, p1sqc, t1GCount, pb1);
    VBox p2 = makePlayerPaneEntry(player2, p2sqc, t2GCount, pb2);

    scorePane.getChildren().add(p1);
    scorePane.getChildren().add(p2);

    return scorePane;
  }

  // A player entry panel for INTERACTIVE mode.
  //
  public VBox makePlayerPaneEntry(Player player, TextField tf) {
    VBox box = new VBox();
    String playerName = player.teamName();

    //pane.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    //pane.setAlignmentY(Component.CENTER_ALIGNMENT);

    tf.setStyle("-fx-font: 24px \"Arial\";");
    Label label = new Label(playerName);
    label.setStyle("-fx-font: 24px \"Arial\";");
    label.setTextFill(player.getSquareColor());
  //  label.setForeground(player.getColor());
    //label.setAlignmentX(Component.CENTER_ALIGNMENT);

    box.getChildren().add(label);
    box.getChildren().add(tf);

    return box;
  }

  public VBox makePlayerPaneEntry(Player player, TextField tf, TextField gc, ProgressBar pb) {
    VBox box = new VBox();
    box.setSpacing(10);
    box.setAlignment(Pos.CENTER);

    String playerName = player.teamName();

  //  box.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // box.setAlignmentY(Component.CENTER_ALIGNMENT);

    Label label = new Label(playerName);
    label.setTextFill(player.getSquareColor());
    label.setStyle("-fx-font: 24px \"Arial\";");
    //label.setForeground(player.getColor());
    //label.setAlignmentX(Component.CENTER_ALIGNMENT);

    box.getChildren().add(label);
    box.getChildren().add(tf);
    if(gc != null) box.getChildren().add(gc);
    box.getChildren().add(pb);

    return box;
  }
}
