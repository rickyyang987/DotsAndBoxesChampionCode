package util;
import javafx.scene.paint.Color;

public class Util {

  // Defaults to an 8 x 8 board. Override on command line.
  //
  public static int N = 8;
  public static int ALL = N * N;
  public static int SLIDER_MAX = 100;
  public static final int DEFAULT_DISPLAY_HEIGHT = 800;
  public static final double ASPECT_RATIO = 1.618;

  public static final double DEFAULT_SECONDS = 0.5;
  public static final long DEFAULT_TIMEOUT = ((long) (DEFAULT_SECONDS * 1000.0));    // 3 thousand milliseconds

  public static final Color NOCOLOR = new Color(.9, .9, .9, 1.0);
  public static final int DEFAULT_GAMES = 25;

  public static final long DEFAULT_DELAY = 1000;  // ms = 1 second

  public static final Color DEFAULT_TILE_COLOR = Color.WHITE; //Color.DODGERBLUE;
  public static final Color PLAYER1_COLOR = Color.DODGERBLUE;
  public static final Color COLOR_FLASH = Color.ORANGERED;
  public static final Color ONE_LINE_TO_GO = Color.YELLOW;//new Color(.9, .9, .9, 1.0);
  public static final Color PLAYER2_COLOR = Color.BROWN;
  public static final Color PLAYER1_LINE_COLOR = new Color(.2, .2, .2, 1.0);
  public static final Color PLAYER2_LINE_COLOR = new Color(.5, .5, .5, 1.0);
  public static final Color UNCLICKED_LINE_COLOR = Color.LIGHTGRAY;

  public static final Score PLAYER1_WINS = new ScoreC(1, 0);
  public static final Score PLAYER2_WINS = new ScoreC(0, 1);
  public static final Score TIE = new ScoreC(0, 0);

  // This is a hack to allow the player to use the Board & Square ADTs
  // without messing up the board display. Wish we had model-view-update.
  //
  public static boolean uiIsOn = true;
  public static void uiOn()  { uiIsOn = true; }
  public static void uiOff() { uiIsOn = false; }

  public static Color makeRandomColor() {
    float
      red   = (float) Math.random(),
      green = (float) Math.random(),
      blue  = (float) Math.random();
    return new Color(red, green, blue, 1.0);
  }

/*
  public static void wait(int pace) {
    try {
      Thread.sleep((int) 5 * pace); //Math.pow(pace, 2)); // currently squaring the pace.
    }
    catch(InterruptedException e) {}
}
*/

  public static void sleep(long ms) {
    try { Thread.sleep(ms); } catch (Exception ignore) {}
  }

  public static String milliToSeconds(long milliseconds) {
    return String.format("%4.1f", ((double) milliseconds));
  }
}
