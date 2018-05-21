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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class GameControlPanel extends VBox implements ControlPanel {

  private Controller controller;
  private DBG dbg;

  private VBox buttonPane;
  private HBox scorePane;

  private TextField team1SQCount, team2SQCount;

  private ProgressBar team1Progress, team2Progress;

  private Slider speedControl;

  public GameControlPanel(Controller controller) {
    Player
      player1 = controller.getPlayer(1),
      player2 = controller.getPlayer(2);

    this.controller = controller;
    this.dbg = new DBG(DBG.UI, "GControlPanel");
    Util util = new Util(controller);

    this.setSpacing(10);
    this.setAlignment(Pos.CENTER);

    this.team1SQCount = new TextField("0");
    this.team2SQCount = new TextField("0");

    team1Progress = new ProgressBar();
    team1Progress.setProgress(0);

    team2Progress = new ProgressBar();
    team2Progress.setProgress(0);

    this.buttonPane = makeButtonPane();
    this.scorePane = util.makeScorePane(team1SQCount, null, team1Progress,
                                        team2SQCount, null, team2Progress);

    //setLayout(new BorderLayout());

    this.getChildren().add(this.buttonPane);
    this.getChildren().add(this.scorePane);
  }

  public ProgressBar getPlayer1PB() { return this.team1Progress; }
  public ProgressBar getPlayer2PB() { return this.team2Progress; }

  private VBox makeButtonPane() {
    VBox buttonPane = new VBox();
    buttonPane.setSpacing(10);
    buttonPane.setAlignment(Pos.CENTER);

    HBox subButtonPane = new HBox();
    subButtonPane.setSpacing(10);
    subButtonPane.setAlignment(Pos.CENTER);

    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font: 24px \"Arial\";");
    startButton.setOnAction(e -> { controller.playOneGame(); });

    Button stopButton = new Button("Stop");
    stopButton.setStyle("-fx-font: 24px \"Arial\";");
    stopButton.setOnAction(e -> { controller.stopResponder(); });

    Button stepButton = new Button("Step");
    stepButton.setStyle("-fx-font: 24px \"Arial\";");
    stepButton.setOnAction(e -> { controller.stepResponder(); });

    subButtonPane.getChildren().add(startButton);
    subButtonPane.getChildren().add(stopButton);
    subButtonPane.getChildren().add(stepButton);

    buttonPane.getChildren().add(subButtonPane);

    this.speedControl = new Slider(0, 1, 0.5);
    speedControl.setPadding(new Insets(10, 25, 10, 25));
    speedControl.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                  double pace = new_val.doubleValue();
                  String msg = String.format("pace set to %03.2f%n", pace);
                  controller.getControlDisplay().setStatus(msg);
                  controller.setPace(pace);
                }
        });

    buttonPane.getChildren().add(this.speedControl);
    return buttonPane;
  }

  public void setButtonPane(VBox buttonPane) {
    this.buttonPane = buttonPane;
  }
  public void setScorePane(HBox scorePane) {
    this.scorePane = scorePane;
  }

  public void setGameScore(Score score) {
    Integer p1 = new Integer(score.getPlayer1());
    Integer p2 = new Integer(score.getPlayer2());

    team1SQCount.setText(p1.toString());
    team2SQCount.setText(p2.toString());
  }

/*  public void setProgress(Clock clock) {
    Player player = clock.getPlayer();
    ProgressBar pb = player.getId() == 1 ? team1Progress : team2Progress;
    long newValue = clock.elapsedTime();
    //pb.setValue((int) newValue);
  }
*/
  public void setMatchScore(Score score) {}
  public int getGames() { return 0; }
}
