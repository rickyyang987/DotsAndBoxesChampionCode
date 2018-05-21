package ui;

import util.*;
import controller.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

public class StatusPane extends VBox {

  private Controller controller;
  private DBG dbg;
  private TextField statusField;

  public StatusPane() {//Controller controller) {
//    this.setPrefWidth(50);
//    this.controller = controller;
//    this.dbg = new DBG(DBG.UI, "StatusPanel");

    this.statusField = new TextField();
    this.statusField.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
    this.getChildren().add(this.statusField);
  }

  public void setStatus(String status) {
    this.statusField.setText(status);
  }
}
