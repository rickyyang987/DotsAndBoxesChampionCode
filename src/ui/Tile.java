package ui;

import util.Side;
import util.Line;
import util.LineC;
import util.Mode;
import util.Util;

import controller.*;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;

public class Tile extends Pane {

  public Horizontal nSide, sSide;
  public Vertical   wSide, eSide;

  private Controller controller;
  private Rectangle background;
  private int size;
  private int row, col;
  private Color unclickedLineColor;

  public Tile(Controller controller, int size, int row, int col, Color color) {
    this.controller = controller;
    this.size = size;
    this.row = row;
    this.col = col;
    this.unclickedLineColor = Util.UNCLICKED_LINE_COLOR;

    int edgeWidth = (int) Math.round(size * .05);

    this.background = new Rectangle(size, size, color);

    Circle circle = new Circle(edgeWidth, unclickedLineColor);

    // Special case circles for the far right side, the bottom and the
    // bottom right corner.
    //
    Circle rightEdge = new Circle(edgeWidth, unclickedLineColor);
    rightEdge.relocate(size - edgeWidth, -edgeWidth);
    Circle bottomEdge = new Circle(edgeWidth, unclickedLineColor);
    bottomEdge.relocate(-edgeWidth, size - edgeWidth);
    Circle bottomRightCorner = new Circle(edgeWidth, unclickedLineColor);
    bottomRightCorner.relocate(size - edgeWidth, size - edgeWidth);

    Horizontal top = new Horizontal(this, size, edgeWidth, unclickedLineColor);
    top.relocate(0, -(edgeWidth / 2));
    this.nSide = top;

    Vertical left = new Vertical(this, edgeWidth, size, unclickedLineColor);
    left.relocate(-(edgeWidth / 2), 0);
    this.wSide = left;

    this.getChildren().addAll(background, top, left, circle);

    if (col == Util.N - 1) {
      Vertical right = new Vertical(this, edgeWidth, size, unclickedLineColor);
      right.relocate(size - (edgeWidth / 2), 0);
      this.eSide = right;
      this.getChildren().add(right);
      this.getChildren().add(rightEdge);
    }
    if (row == Util.N - 1) {
      Horizontal bottom = new Horizontal(this, size, edgeWidth, unclickedLineColor);
      bottom.relocate(0, size - (edgeWidth / 2));
      this.sSide = bottom;
      this.getChildren().add(bottom);
      this.getChildren().add(bottomEdge);
    }
    if (row == Util. N - 1 && col == Util.N - 1)
      this.getChildren().add(bottomRightCorner);
 }
  public Group getSide(Side side) {
    switch (side) {
    case NORTH: return this.nSide;
    case SOUTH: return this.sSide;
    case WEST:  return this.wSide;
    default:    return this.eSide;
    }
  }

  public Controller getController() { return this.controller; }

  // The select function is called from board.markLine after it
  // is determined that the player owning the color owns the
  // corresponding square.
  //
  public void select(Color color) {
    setColor(color);
  }

  public void setColor(Color color) {
    this.background.setFill(color);
  }

  public int getRow() { return this.row; }
  public int getCol() { return this.col; }
}
