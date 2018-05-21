package ui;

import util.*;
import controller.*;
import players.*;
import players.player1.*;
import players.player2.*;

import java.util.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


public class MatchControlPanel extends VBox implements ControlPanel {

  private Controller controller;
  private DBG dbg;

  private VBox buttonPane;
  private HBox scorePane;
  private TextField team1SQCount, team2SQCount;
  private TextField t1GCount, t2GCount;
  private TextField games;
  private ProgressBar team1Progress, team2Progress;

  private Slider speedControl;

  public MatchControlPanel(Controller controller) {
    Player
      player1 = controller.getPlayer(1),
      player2 = controller.getPlayer(2);

    this.controller = controller;
    this.dbg = new DBG(DBG.UI, "MControlPanel");
    Util util = new Util(controller);

    this.team1SQCount = makeTextField("0");
    this.team2SQCount = makeTextField("0");

    this.t1GCount = makeTextField("0");
    this.t2GCount = makeTextField("0");

    team1Progress = new ProgressBar();
    team1Progress.setProgress(0);

    team2Progress = new ProgressBar();
    team2Progress.setProgress(0);

    this.buttonPane = makeButtonPane();

    this.scorePane = makeTeamPanes(player1, player2);

    this.getChildren().addAll(this.buttonPane, this.scorePane);
  }

  private VBox makeButtonPane() {
    VBox buttonPane = new VBox();
    buttonPane.setPadding(new Insets(60, 0, 30, 0));

    HBox subButtonBox = new HBox();
    subButtonBox.setSpacing(20);
    subButtonBox.setAlignment(Pos.CENTER);
    subButtonBox.setPadding(new Insets(10, 0, 20, 0));

    games = makeTextField(new Integer(util.Util.DEFAULT_GAMES).toString());

    Button startButton = new Button("Start");
    startButton.setOnAction( e -> {
      System.out.println("in action listener for Match start button.");
      controller.playMatch();
    });

    Button stopButton = new Button("Stop");
    stopButton.setOnAction(e -> {
      controller.stopResponder();
    });

    subButtonBox.getChildren().addAll(startButton, games, stopButton);

    buttonPane.getChildren().add(subButtonBox);

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

  // DITCH THESE?
  public void setButtonPane(VBox buttonPane) { this.buttonPane = buttonPane; }
  public void setScorePane(HBox scorePane)   { this.scorePane = scorePane; }

  public void setGameScore(Score score) {
    Integer p1 = new Integer(score.getPlayer1());
    Integer p2 = new Integer(score.getPlayer2());

    team1SQCount.setText(p1.toString());
    team2SQCount.setText(p2.toString());
  }

  public int getGames() { return Integer.parseInt(games.getText()); }

  public void setMatchScore(Score games) {
    Integer p1 = new Integer(games.getPlayer1());
    Integer p2 = new Integer(games.getPlayer2());

    t1GCount.setText(p1.toString());
    t2GCount.setText(p2.toString());
  }

  public ProgressBar getPlayer1PB() { return team1Progress; }
  public ProgressBar getPlayer2PB() { return team2Progress; }

  private TextField makeTextField(String txt) {
    TextField tf = new TextField(txt);
    tf.setPrefWidth(60);
    tf.setAlignment(Pos.CENTER);
    tf.setStyle("-fx-font: 18px \"Arial\";");
    return tf;
  }

  private HBox makeLabeledField(String fieldLabel, TextField tf) {
    HBox hb = new HBox();
    //hb.setStyle("-fx-border-color: red;");
    hb.setPadding(new Insets(5, 0, 5, 0));
    hb.setSpacing(10);
    hb.setAlignment(Pos.BASELINE_RIGHT);

    Label label = new Label(fieldLabel);
    label.setStyle("-fx-font: 20px \"Arial\";");

    hb.getChildren().addAll(label, tf);
    return hb;
  }

  private VBox makeLabeledFields(TextField boxes, TextField games) {
    VBox vb = new VBox();
    vb.setSpacing(10);
    //vb.setPadding(new Insets(0, 10, 0, 10));
    //vb.setStyle("-fx-border-color: black;");
    vb.setAlignment(Pos.CENTER);
    HBox boxesBox = makeLabeledField("Boxes", boxes);
    HBox gamesBox = makeLabeledField("Games", games);
    vb.getChildren().addAll(boxesBox, gamesBox);
    return vb;
  }

  private VBox makeTeamPane(String name, TextField boxes,
                             TextField games, ProgressBar progressBar) {
    VBox vb = new VBox();
    vb.setSpacing(20);
    vb.setAlignment(Pos.CENTER);
    vb.setStyle("-fx-background-color: lightgrey");
    vb.setPadding(new Insets(20, 20, 20, 20));

    Label teamName = new Label(name);
    teamName.setStyle("-fx-font: 20px \"Arial\";");

    VBox fields = makeLabeledFields(boxes, games);

    vb.getChildren().addAll(teamName, fields, progressBar);
    return vb;
  }

  private HBox makeTeamPanes(Player p1, Player p2) {
    HBox hb = new HBox();
    hb.setSpacing(10);
    hb.setAlignment(Pos.CENTER);
    String name = p1.teamName();
    VBox team1 = makeTeamPane(name, team1SQCount, t1GCount, team1Progress);
    name = p2.teamName();
    VBox team2 = makeTeamPane(name, team2SQCount, t2GCount, team2Progress);
    hb.getChildren().addAll(team1, team2);
    return hb;
  }
}
