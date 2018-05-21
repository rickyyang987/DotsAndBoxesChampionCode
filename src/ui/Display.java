package ui;

import controller.*;
import board.*;
import util.*;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class Display extends HBox {

  private BoardDisplay bd;
  private ControlDisplay cd;
  private Controller controller;
  private int height;

  public Display(Board board, Controller controller, int width, int height) {
    this.height = height;
    this.controller = controller;
    this.setPadding(new Insets(8, 8, 8, 8));
    this.setSpacing(8);
    this.bd = new BoardDisplay(board, controller, height);
    this.cd = new ControlDisplay(controller, width, height);
    this.getChildren().addAll(bd, cd);
  }

  public Display(Board board, Controller controller, ControlDisplay cd, int width, int height) {
    this.height = height;
    this.controller = controller;
    this.setPadding(new Insets(8, 8, 8, 8));
    this.setSpacing(8);
    this.bd = new BoardDisplay(board, controller, height);
    this.cd = cd;
    this.getChildren().addAll(bd, cd);
  }

  public BoardDisplay   getBoardDisplay()   { return this.bd; }
  public ControlDisplay getControlDisplay() { return this.cd; }
  public void setStatus(String msg) {
    getControlDisplay().getStatusPane().setStatus(msg);
  }

  // The installBoard function is called from the MATCH mode code
  // in ControllerC.
  //
  public void installBoard(Board board) {
    this.bd = new BoardDisplay(board, controller, height);
  }
}
