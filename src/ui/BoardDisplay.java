package ui;

import controller.*;
import util.Side;
import util.Line;
import util.LineC;
import util.DBG;
import util.Util;
import board.Board;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class BoardDisplay extends GridPane {

  private int displaySide;
  Controller controller;
  private Tile[][] tiles;    // NB: A ui.Tile is an image of a board.Square.
  private DBG dbg;

  public BoardDisplay(Board board, Controller controller, int displaySide) {
    this.controller = controller;
    this.displaySide = displaySide;

    this.setPrefWidth(displaySide);

    controller.setBoardDisplay(this);

    this.dbg = new DBG(DBG.UI, "BoardDisplay");

    Color color = Util.DEFAULT_TILE_COLOR;

    this.tiles = new Tile[Util.N][Util.N];

    int tileSize = (int) (displaySide / Util.N);
    System.out.format("BoardDisplay: tileSize = %d\n", tileSize);

    for(int row = 0; row < Util.N; row++)
      for(int col = 0; col < Util.N; col++) {
        Tile tile = new Tile(controller, tileSize, row, col, color);
        this.tiles[row][col] = tile;
        this.add(tile, col, row);
      }
  }

  // Actually color a tile. Called from SquareC.java & BoardC
  //
  public void colorTile(board.Square square, Color color) {

    // The following test is here because we want the player to have access
    // to the Board and Square ADTs. Unfortunately, some of the operations in
    // those ADTs, in addition to working with the model, also cause the board
    // display to be updated. So before the player's makePlay function is
    // called, the ui is turned off. It's turned back on after the call.
    //
    if (!Util.uiIsOn) return;

    int row = square.getRow(),
        col = square.getCol();
    Tile tile = this.tiles[row][col];
    tile.setColor(color);
  }

  // Actuall color a line. Called from SquareC.java
  //
  public void colorSelectedLine(Line line, Color color) {

    // The following test is here because we want the player to have access
    // to the Board and Square ADTs. Unfortunately, some of the operations in
    // those ADTs, in addition to working with the model, also cause the board
    // display to be updated. So before the player's makePlay function is
    // called, the ui is turned off. It's turned back on after the call.
    //
    if (!Util.uiIsOn) return;

    Side side = line.getSide();
    int row = line.getRow(),
        col = line.getCol();

    // In the following we're dealing with the fact that tiles only
    // have lines on top (NORTH) and to the left (WEST).
    //
    if (col != Util.N - 1 && side == Side.EAST) {
      col++;
      side = Side.WEST;
    }
    if (row != Util.N - 1 && side == Side.SOUTH) {
      row++;
      side = Side.NORTH;
    }

    Tile tile = this.tiles[row][col];

    if (side == Side.WEST || side == Side.EAST)
      ((Vertical)(tile.getSide(side))).select(color);

    if (side == Side.NORTH || side == Side.SOUTH)
      ((Horizontal)(tile.getSide(side))).select(color);
  }
}
