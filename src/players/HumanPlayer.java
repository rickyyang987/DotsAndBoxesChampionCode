package players;

import util.*;
import board.*;
import javafx.scene.paint.Color;

public class HumanPlayer implements Player {

    private String name;
    private Color color;
    private int id;

    public HumanPlayer() {
        this.id = 0;
        this.name = "Human";
        this.color = Color.ORANGE;
    }

    // This is a dummy routine that won't be called.
    //
    public Line makePlay(Board board, Line oppPlay, long ms) {
        return new LineC(0, 0, Side.NORTH);
    }
    public String teamName() { return this.name; }
    public Color getSquareColor() { return this.color; }
    public Color getLineColor() { return Color.BLACK; }
    public String teamMembers() { return "Human"; }
    public int getId() { return this.id; }
    public String toString() { return "Human"; }
}
