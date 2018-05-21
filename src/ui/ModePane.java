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
import javafx.scene.control.ComboBox;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ModePane extends FlowPane {

  private Controller controller;
  private Mode mode;
  private DBG dbg;
  private Color color;

  public ModePane(Controller controller, Mode mode) {
    this.setAlignment(Pos.CENTER);
    this.setPadding(new Insets(20, 20, 20, 20));
    this.controller = controller;
    this.mode = mode;
    this.dbg = new DBG(DBG.UI, "ModePanel");

    this.setStyle("-fx-background-color: #F0F0F0");

    final ComboBox<String> cb = new ComboBox<String>();
    cb.setStyle("-fx-font: 24px \"Arial\";");
    cb.getItems().addAll("Interactive", "Game", "Match");
    cb.setValue(mode + "");

    cb.valueProperty().addListener(new ChangeListener<String>() {
        @Override public void changed(ObservableValue<? extends String> ov, String oldMode, String newMode) {
          if(newMode.equals("Interactive"))
            controller.modeReset(Mode.INTERACTIVE);
          else if (newMode.equals("Game"))
            controller.modeReset(Mode.GAME);
          else
            controller.modeReset(Mode.MATCH);
        }
      });
     this.getChildren().add(cb);
  }
}
