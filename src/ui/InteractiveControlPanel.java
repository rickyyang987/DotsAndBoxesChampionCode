package ui;

import util.*;
import controller.*;
import players.*;
import players.player1.*;
import players.player2.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;


public class InteractiveControlPanel extends GridPane implements ControlPanel {


  private Controller controller;
  private DBG dbg;

  private VBox buttonPane;
  private HBox scorePane;
  public TextField team1SQCount, team2SQCount;

  public InteractiveControlPanel(Controller controller) {
    this.controller = controller;
    this.dbg = new DBG(DBG.UI, "IControlPanel");
    Util util = new Util(controller);

    this.setAlignment(Pos.CENTER);
    this.setPadding(new Insets(5, 5, 5, 5));
    //this.setSpacing(10);
    this.setStyle("-fx-border-color: orange");
    this.setStyle("-fx-background-color: #A0A0A0");

    this.team1SQCount = new TextField("0");

    if(dbg.debug)
      dbg.println("team1SQCount hashCode at creation = " + team1SQCount.hashCode());

    this.team2SQCount = new TextField("0");

    this.buttonPane = makeButtonPane();
    this.scorePane =  util.makeScorePane(team1SQCount, team2SQCount);

    this.add(this.buttonPane, 0, 0);
    this.add(this.scorePane, 0, 1);
  }

  private VBox makeButtonPane() {
    VBox buttonPane = new VBox();
    buttonPane.setAlignment(Pos.CENTER);
    buttonPane.setPadding(new Insets(5, 5, 10, 5));

    Button stepButton = new Button("Step");
    stepButton.setStyle("-fx-font: 24px \"Arial\";");
    stepButton.setOnAction(e -> { controller.stepResponder();});

    buttonPane.getChildren().add(stepButton);
    return buttonPane;
  }

  public void actionPerformed(ActionEvent e) {
    dbg.println("stepButton just clicked in INTERACTIVE mode.");
    controller.stepResponder();
  }

  public void setButtonPane(VBox buttonPane) {
    this.buttonPane = buttonPane;
  }
  public void setScorePane(HBox scorePane) {
    this.scorePane = scorePane;
  }

  public void setGameScore(Score score) {

    if(dbg.debug) {
      dbg.println("setGameScore: attempting to set " + score);
      dbg.println("setGameScore: team1SQCount hashCode at setText = " + team1SQCount.hashCode());
    }
    Integer p1 = new Integer(score.getPlayer1());
    Integer p2 = new Integer(score.getPlayer2());

    team1SQCount.setText(p1.toString());
    team2SQCount.setText(p2.toString());
  }

  public void setMatchScore(Score gs) {}   // There is only one game.
  public int getGames() { return 0; }
  public void setProgress(Clock nothing) {}
  public ProgressBar getPlayer1PB() { return null; }
  public ProgressBar getPlayer2PB() { return null; }


  class StepListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      controller.stepResponder();
    }
  }
}
