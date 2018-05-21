package board;

import util.Side;
import util.Line;
import util.LineC;
import util.Option;
import util.None;

import ui.*;
import players.*;
import java.util.*;
import controller.*;
import javafx.scene.paint.Color;

public class SquareC implements Square {

  private Controller controller;
  private final Option<Player> noOne = new None<Player>();

  private Option<Player> owner;
  private boolean north;
  private boolean west;
  private boolean south;
  private boolean east;

  private int row, col;

  public SquareC(Controller controller, int row, int col, Option<Player> owner,
                 boolean north, boolean west, boolean south, boolean east) {
    this.controller = controller;
    this.row = row;
    this.col = col;
    this.owner = owner;
    this.north = north;
    this.west  = west;
    this.south = south;
    this.east  = east;
  }

  public SquareC(Controller controller, int row, int col) {
    this(controller, row, col, new None<Player>(), false, false, false, false);
  }

  // Getters
  //
  public int getRow() { return this.row; }
  public int getCol() { return this.col; }

  public Option<Player> getOwner() { return this.owner; }

  public boolean get(util.Side side) {
    switch (side) {
    case NORTH : return this.north;
    case WEST  : return this.west;
    case SOUTH : return this.south;
    default    : return this.east;
    }
  }

  // Setters
  //
  public void claimFor(Option<Player> po) {
    this.owner = po;
    Color color = po.valueOf().getSquareColor();
    BoardDisplay bd = this.controller.getDisplay().getBoardDisplay();
    bd.colorTile(this, color);
  }

  public void setSide(util.Side side, boolean value) {
    switch (side) {
    case NORTH : this.north = value; break;
    case WEST  : this.west  = value; break;
    case SOUTH : this.south = value; break;
    default    : this.east  = value;
    }
  }

  public boolean sideIsMarked(util.Side side) { return get(side); }

  // This one is for claiming.
  //
  public boolean hasFourSides() { return hasNMarkedSides(4); }

  // This one is just for shading.
  //
  public boolean hasThreeSides() { return hasNMarkedSides(3); }

  // The markLine function is called by Board.markLine.
  //
  public void markLine(Line line, Player player) {
    // The logic preventing double marking of a side is handled
    // in the mark function of the Board type.  CHECK THIS!
    //
    Side side = line.getSide();
    this.setSide(side, true);

    Color lineColor = player.getLineColor();
    BoardDisplay bd = controller.getDisplay().getBoardDisplay();
    bd.colorSelectedLine(line, lineColor);
  }

  public boolean isOwned() { return !(this.getOwner().equals(this.noOne)); }

  public Set<Line> openLines() {
    int row = this.getRow(),
        col = this.getCol();
    Set<Line> lineSet = new HashSet<Line>();

    if(!sideIsMarked(Side.NORTH)) lineSet.add(new LineC(row, col, Side.NORTH));
    if(!sideIsMarked(Side.WEST))  lineSet.add(new LineC(row, col, Side.WEST));
    if(!sideIsMarked(Side.SOUTH)) lineSet.add(new LineC(row, col, Side.SOUTH));
    if(!sideIsMarked(Side.EAST))  lineSet.add(new LineC(row, col, Side.EAST));

    return lineSet;
  }
  public String toString() {
    String
      a = "{row =" + this.row + ", col =" + this.col,
      b = ", north=" + this.north + ", west=" + this.west,
      c = ", south=" + this.south + ", east=" + this.east,
      d = ", owner=" + this.owner.toString() + "}";
    return a + b + c + d;
  }

  public boolean hasNMarkedSides(int n) {
    int count = 0;
    if(this.sideIsMarked(Side.NORTH)) count++;
    if(this.sideIsMarked(Side.WEST)) count++;
    if(this.sideIsMarked(Side.SOUTH)) count++;
    if(this.sideIsMarked(Side.EAST)) count++;
    return n == count;
  }

  @Override
  public Square clone() {
    return new SquareC(this.controller, this.row, this.col, this.owner,
                       this.north, this.west, this.south, this.east);
  }

}
