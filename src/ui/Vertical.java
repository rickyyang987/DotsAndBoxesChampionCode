package ui;

import controller.*;
import util.Mode;
import util.Line;
import util.*;
import util.Util;
import util.Side;
import board.Board;
import players.HumanPlayer;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.shape.Rectangle;

public class Vertical extends Group {

  private final Canvas canvas;
  private GraphicsContext gc;
  private int width, height;

  public Vertical(Tile tile, int width, int height, Color color) {

    this.width = width;
    this.height = height;
    this.canvas = new Canvas(width, height);
    this.gc = canvas.getGraphicsContext2D();

    this.gc.setFill(color);
    this.gc.fillRect(0, 0, width, height);
    this.getChildren().add(this.canvas);

    this.setOnMouseClicked(e -> {
      Line line;
      Controller controller = tile.getController();
        if (controller.getMode() == Mode.INTERACTIVE) {
          int row = tile.getRow(),
              col = tile.getCol();

          // NB: The human has clicked on a line in INTERACTIVE
          // mode. We're holding off on actually marking this
          // until we see whether or not board.markLine is OK
          // with it. If so, it will call the select function
          // below to colorize it. This allows us to handle
          // colorizing lines in a common way for both mouse clicks
          // and program-generated line selections.
          // ALSO: issue of possibly colorizing the tile is left
          // to the board.markLine function.
          //
          if (col == Util.N - 1 && this == tile.eSide)
            line = new LineC(row, col, Side.EAST);
          else
            line = new LineC(row, col, Side.WEST);
          controller.makeHumanPlay(line);
        }
      });
  }

  public void select(Color color) {
    this.gc.setFill(color);
    this.gc.fillRect(0, 0, this.width, this.height);
  }
}
