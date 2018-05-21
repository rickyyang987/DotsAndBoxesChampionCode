package ui;

import util.*;
import controller.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;


public class ControlDisplay extends BorderPane {

  private Controller controller;
  private StatusPane statusPane;
  private ModePane modePane;
  private ControlPanel controlPanel;
  private Mode mode;
  private int height;
  private DBG dbg;
  private Util util;

  public ControlDisplay(Controller controller, int width, int height) {
    //this.setStyle("-fx-border-color: brown");
    this.setStyle("-fx-background-color: grey");
    //this.setPadding(new Insets(5, 5, 5, 5));

    this.setPrefHeight(height);
    this.setPrefWidth(width - height);

    this.controller = controller;
    this.height = height;
    this.mode = controller.getMode();

    controller.setControlDisplay(this);    // Is this still needed?

    this.dbg = new DBG(DBG.UI, "ControlDisplay");
    this.util = new Util(controller);

    this.controlPanel = util.makeControlPanel(controller, mode);

    this.modePane = new ModePane(controller, mode);
    this.statusPane = new StatusPane();

    this.setTop(this.modePane);

    if (this.mode == Mode.INTERACTIVE) {
      InteractiveControlPanel icp = (InteractiveControlPanel) this.controlPanel;
      this.setCenter((Pane) icp);
    }
    else if (mode == Mode.GAME) {
      GameControlPanel gcp = (GameControlPanel) this.controlPanel;
      this.setCenter((Pane) gcp);
      }
    else if (mode == Mode.MATCH) {
      MatchControlPanel mcp = (MatchControlPanel) this.controlPanel;
      this.setCenter((Pane) mcp);
    }

    this.setBottom(this.statusPane);
    controlPanel.setGameScore(new ScoreC(0, 0));

    setStatus("A " + milliToSeconds(controller.getTimeout()) + " second game.");
  }

  // Getters
  //
  public ModePane getModePane()         { return this.modePane; }
  public ControlPanel getControlPanel() { return this.controlPanel; }
  public StatusPane getStatusPane()     { return this.statusPane; }
  //public int getHeight()                { return this.height; }
  public int getGames()                 { return this.controlPanel.getGames(); }

  // Setters
  //
  // Of the three panels, only the control panel can be replaced.
  //
  public void setControlPanel(ControlPanel cp) { this.controlPanel = cp; }

  public void setStatus(String status)  { this.statusPane.setStatus(status); }
  //public void setProgress(Clock clock)  { this.controlPanel.setProgress(clock); }
  public void setGameScore(Score score) { this.controlPanel.setGameScore(score); }
  public void setMatchScore(Score score) { this.controlPanel.setMatchScore(score); }

  // The board display should be square so we'll make this control display
  // consume the remainder of the width of the rectangle.
  //
  public Dimension getPreferredSize() {
    return new Dimension((int) (this.height / 1.618), 0);
  }
  public static String milliToSeconds(long milliseconds) {
    return String.format("%4.1f", ((double) milliseconds / 1000.));
  }
}
