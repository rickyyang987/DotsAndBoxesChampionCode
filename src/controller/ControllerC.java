package controller;

import java.io.File;
import ui.BoardDisplay;
import ui.ControlDisplay;
import ui.GameControlPanel;
import ui.ControlPanel;
import ui.Display;
import util.*;
import board.*;
import players.*;
import players.player1.*;
import players.player2.*;

import java.util.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.concurrent.*;

import javafx.application.Application;

@SuppressWarnings("unchecked")

public class ControllerC extends Pane implements Controller {

  private Stage stage;
  private Mode mode;
  private Board board;
  private Player player1, player2;
  private boolean interactive;
  private Set<board.Square> claimedSquares;
  private Line oppPlay;
  private Score score;
  private Score games;
  private boolean stop;
  private DBG dbg;
  //private Container contentPane;
  private BoardDisplay boardDisplay;
  private ControlDisplay controlDisplay;
  private Display display;
  private double pace;
  private int game;     // game # in MATCH mode
  private long delay;
  private long timeout;
  private Controller controller;
  private int width, height;
  private PlayService service;

  public ControllerC(Stage stage, Mode mode, int width, int height) {
    this.stage = stage;
    this.width = width;
    this.height = height;
    this.controller = this; // needed for inner class below
    this.dbg = new DBG(DBG.CONTROL, "ControllerC");
    this.initialize(mode);
  }

  // Most of the work normally done in the constructor is factored out into
  // this initialize function because this work also needs to be done on a
  // reset.
  public void initialize(Mode mode) {
    this.mode = mode;
    this.delay = (long) (Util.DEFAULT_DELAY * (1.0 - this.pace));
    this.timeout = Util.DEFAULT_TIMEOUT;

    // Make a fresh board.
    this.board = new BoardC(this, Util.N, Util.N);

    // Set up a new UI; with board on the left, control on the right
    // this will be included in a Scene and Staged either in the boot
    // code in DotsAndBoxes or below in reset.
    //
    // Now the players. NB that in INTERACTIVE mode, player2 is human.
    this.player1 = new Player1();
    if (this.player1 == null)
      System.out.format("ControllerC: player1 = null%n");
    setPlayer2(mode);
    if (this.player2 == null)
      System.out.format("ControllerC: player2 = null%n");

    this.display = new Display(board, this, width, height);

    this.score = new ScoreC(0, 0);
    this.games = new ScoreC(0, 0);

    this.oppPlay = null;

    String message = String.format("%s mode.", mode);
    getDisplay().setStatus(message);
    if(dbg.debug) dbg.println(message);
  }

  // This is the action listener that is called by the UI in the case
  // of a mode reset.
  //
  public void modeReset(Mode mode) {
    // Among other things, initialize makes a new display.
    this.initialize(mode);

    // Make a new Scene and put it on the stage.
    //
    Scene newScene = new Scene(this.getDisplay(), this.width, this.height);
    this.stage.setScene(newScene);
  }

  // Getters
  //
  public Display getDisplay()               { return this.display; }
  public Board getBoard()                   { return this.board; }
  public Mode getMode()                     { return this.mode; }
  public long getTimeout()                  { return this.timeout; }
  public ControlDisplay getControlDisplay() { return this.controlDisplay; }
  public Player getPlayer(int i) {
    if (i == 1 && this.player1 != null)
      return this.player1;
    else if (i == 2 && this.player2 != null)
      return this.player2;
    else
      throw new RuntimeException("getPlayer: there is no player=" + i);
  }

  // Various setters.
  //
  public void setBoard(Board board)                   { this.board = board; }
  public void setBoardDisplay(BoardDisplay bd)        { this.boardDisplay = bd; }
  public void setControlDisplay(ControlDisplay cd)    { this.controlDisplay = cd; }
  public void setPace(double sliderPace) {
    this.pace = sliderPace;
    this.delay = (long) (Util.DEFAULT_DELAY * (1.0 - this.pace));
  }
  public void setPlayer2(Mode mode) {
    player2 = (mode == Mode.INTERACTIVE) ? new HumanPlayer() : new Player2();
  }

  /////////////////////////////////////////////////////////////////////////
  //
  // INTERACTIVE MODE
  //
  // This is the action listener that is called by the UI when the human
  // being uses the mouse to select a line.
  //
  public void makeHumanPlay(Line line) {
    if(this.dbg.debug)
      dbg.println("makeHumanPlay: attempting to play " + line);

    this.oppPlay = line;
    try {
      // One line can claim 0, 1 or 2 squares. NB Board.markLine
      // takes care of UI calls to color lines & captured squares
      // if there are any. The model logic is handled below.
      // This is in a try-block because Board.markLine throws an
      // exception if the line is already marked.
      //
      this.claimedSquares = board.markLine(this.player2, line);

      if(this.dbg.debug)
        System.out.println("makeHumanPlay: claiming squares " + this.claimedSquares);

      score.add(this.player2, this.claimedSquares.size());

      controlDisplay.setGameScore(score);
      if(this.dbg.debug) dbg.println("makeHumanPlay: score=" + score);
    }
    catch (RuntimeException msg) {
      modeReset(this.mode);
      controlDisplay.setStatus(msg.getMessage());
    }
    if(board.gameOver()) showWinner(this.score, null);
  }

  // This is the action listner for the step button in INTERACTIVE mode.
  // It plays the Player1's turn in interacive mode.
  //
  public void stepResponder() {

    boolean filledASquare = true;
    Line line = null;

    while(filledASquare && !board.gameOver()) {
      try {
        Thread.sleep(Util.DEFAULT_DELAY);
        line = this.player1.makePlay(board, this.oppPlay, 0);
        if(this.dbg.debug)
          dbg.println("stepResponder: player1 attempting to play " + line);
      }
      catch (Exception msg) {
        controlDisplay.setStatus(msg.getMessage());
      }
      try {
        this.claimedSquares = board.markLine(this.player1, line);

        if(this.dbg.debug) {
          dbg.println("stepResponder: " + this.claimedSquares.size() + " squares claimed.");
        }
        filledASquare = !this.claimedSquares.isEmpty();

        if (filledASquare) {
          score.add(this.player1, this.claimedSquares.size());

          controlDisplay.setGameScore(score);

          dbg.println("stepResponder: score=" + score);
        }
      }
      catch (RuntimeException msg) {
        modeReset(this.mode);
        controlDisplay.setStatus(msg.getMessage());
      }
    }
    if(board.gameOver()) showWinner(this.score, null);
  }

  // showWinner is called for all three modes. From INTERACTIVE
  // or GAME mode the wins input will be null. From MATCH mode
  // the wins input is the number of wins for each player.
  //
  private void showWinner(Score score, Score wins) {
    int p1 = score.getPlayer1(),
        p2 = score.getPlayer2();
    if (p1 > p2) {
      controlDisplay.setStatus(player1.teamName() + " wins.");
      if (wins != null) wins.add(Util.PLAYER1_WINS);
    }
    else if (p1 < p2) {
      controlDisplay.setStatus(player2.teamName() + " wins.");
      if (wins != null) wins.add(Util.PLAYER2_WINS);
    }
    else
      controlDisplay.setStatus("This game ended in a tie.");

    if (wins == null) return;

    controlDisplay.setMatchScore(wins);
  }

  ////////////////////////////////////////////////////////////////////////
  //
  // GAME MODE
  //
  // This is the action listner for the Start button in GAME mode.
  //
  public void playOneGame() {

    this.stop = false;
    this.oppPlay = null;

    System.out.format("%nplayOneGame: pace = %3.2f.%n", this.pace);

    ControlPanel cp = controlDisplay.getControlPanel();

    ProgressBar
      p1Progress = cp.getPlayer1PB(),
      p2Progress = cp.getPlayer2PB();

    // The timed players here are either atBat or onDeck.
    //
    System.out.format("playOneGame: setting timeout = %d.%n", getTimeout());
    long timeout = getTimeout();    // ms

    TimedPlayer
      player1 = new TimedPlayerC(this.player1, timeout, p1Progress),
      player2 = new TimedPlayerC(this.player2, timeout, p2Progress);

    Players players = new Players(player1, player2);

    // The following service handler is the main driver of the game.
    // It is called whenever a player executed in a non-IU blocking
    // service thread choses a Line within their timeout period.
    //
    this.service = new PlayService(players, this.board, this.oppPlay);

    service.setOnSucceeded( t -> {
      System.out.format("In OnSucceeded.%n");
      boolean filledASquare;

      // The fact that we're in the OnSucceeded handler means that the player
      // atBat chose a line within the allocated time (i.e., their clock
      // didn't expire and they didn't throw an exception). If either of these
      // had occurred we'd be in the OnFailed handler.
      //
      // At this point, we don't know if the chosen line is legal or whether
      // or not it claims any squares. If it's legal and it claimed a square
      // (or two), then the atBat player will get another crack at it if the
      // board isn't full. If the play was legal but didn't claim any squares
      // then the onDeck player is atBat and the atBat player is onDeck.
      //
      Player player = players.atBat.getPlayer();
      String name = player.teamName();

      // The line is chosen on a timeout-sensitive, non UI-blocking background
      // service thread.
      //
      Line line = (Line) t.getSource().getValue();
      this.oppPlay = line;

      System.out.format("in UI thread: %s chose line = %s.%n", name, line);

      // A line may claim 0, 1 or 2 squares. Board.markLine takes care of the
      // UI calls required to color the lines & captured squares if there are
      // any. The model logic is handled below. This is in a try-block because
      // Board.markLine throws an exception if the chosen line was already
      // marked.
      //
      try {
        this.claimedSquares = board.markLine(player, line);
      }
      catch (Exception e) {

        // We're probably here because the chosen line was already marked.
        // If so, the player that chose it loses the game.
        //
        controlDisplay.setStatus(player.teamName() + " lost by rule violation.");
        service.cancel();
        return;
      }

      // The atBat player chose a legal line. Update the Score.
      //
      score.add(player, this.claimedSquares.size());
      controlDisplay.setGameScore(score);

      // Update the progress bar. We can't use the built-in progress update
      // gizmo for services. Or rather, we don't know how to use it in this
      // slightly non-standard situation where we have two progress bars.
      // We'll do the progress update by hand.
      //
      long timeRemaining = players.atBat.timeRemaining();
      double progress = 1.0 - timeRemaining / (double) this.timeout;
      System.out.format("Setting progress for %s to %f%n", player.teamName(), progress);
      players.atBat.getProgressBar().setProgress(progress);

      if (board.gameOver()) {
        showWinner(score, null);
        service.cancel();
        return;
      }
      else {
        filledASquare = !this.claimedSquares.isEmpty();

        if (!filledASquare) {
          players.swap();
          service.setAtBat(players.atBat, oppPlay);
          System.out.format("No squares filled, swapped players. ");
          System.out.format("Now atBat = %s, onDeck = %s.%n", players.atBat.teamName(), players.onDeck.teamName());
        }
        else
          System.out.format("In OnSucceeded: %s gets to go again.%n", name);

        if (!this.stop) {

          // The pace is set by the slider in the UI. Want to improve this by
          // including a slow start, fast middle, slow end by introducing a
          // sinusoidal coefficient:
          // 1.0 - Math.sin((elapsedTime / totalTime) * Math.PI)
          // I don't think this.delay is being update when the slider in the
          // UI is changed... FIGURE THIS OUT
          //
          Util.sleep(delay);
          if (service.getState() == Worker.State.READY)
            service.start(); // NOT CLEAR TO ME THIS ISN'T DEAD CODE
          else
            service.restart();
        }
      }
    });

    service.setOnFailed(t -> {
      System.out.format("In OnFailed.%n");
      String
        name = players.atBat.teamName(),
        message = "Game over. " + name + " threw an exception.";
      if (dbg.debug) dbg.println(message);
      controlDisplay.setStatus(message);
      service.cancel();
    });

    service.setOnCancelled(t -> {
      System.out.format("In OnCancelled.%n");
      if (dbg.debug) dbg.println("playOneGame: in OnCancelled handler.%n");
    });

    System.out.format("In playOneGame, starting on the PlayService.%n");
    service.start();
  }

  // This is the event listener for the stop button. NB: it toggles.
  //
  public void stopResponder() {
    this.stop = !this.stop;
    if (!this.stop) service.restart();
  }

// The javafx Service class allows us to run threads in such a way that
// they don't block the UI thread.
//
class PlayService extends Service<Line> {

  TimedPlayer player;
  String name;
  Board board;
  Line oppPLay;

  PlayService(Players players, Board board, Line oppPLay) {
    this.player = players.atBat;
    this.name = this.player.teamName();
    this.board = board;
    this.oppPLay = oppPLay;
  }

  // This setter is required because we don't want the batter to be fixed
  // at service creation time, it should be determined by whether or not
  // the previous player won a square.
  //
  public void setAtBat(TimedPlayer atBat, Line oppPlay) {
    this.player = atBat;
    this.name = this.player.teamName();
    this.oppPLay = oppPLay;
  }

  // resetBoard is required in MATCH mode in order to replace the old
  // board with a fresh one.
  //
  public void resetBoard(Board board) {
    this.board = board;
  }

  @Override
  protected Task createTask() {
    return new Task<Line>() {
      String msg;
      @Override
      protected Line call() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        TimedTask task = new TimedTask(player, board, oppPLay);
        Future<Line> future = executor.submit(task);

        try {
          long ms = player.timeRemaining();
          if (dbg.debug) {
            msg = String.format("%nPlay service: %s atBat, %d ms.%n", name, ms);
            dbg.println(msg);
          }

          Line line = future.get(ms, TimeUnit.MILLISECONDS);
          ms = player.timeRemaining();
          if (dbg.debug) dbg.println("Play service: timed task succeeded.");
          //updateProgress(ms, getTimeout());
          return line;
        }
        catch (ExecutionException | TimeoutException | InterruptedException e) {
          future.cancel(true);
          System.out.println("PlayService: in exception handler, terminated.");
          throw new Exception("Failed in timed task.");
        }
      }
    };
  }
}

class TimedTask implements Callable<Line> {

  TimedPlayer player;
  String name;
  Board board;
  Line oppPlay;

  TimedTask(TimedPlayer player, Board board, Line oppPlay) {
    this.player = player;
    this.name = player.teamName();
    this.board = board;
    this.oppPlay = oppPlay;
  }

  @Override
  public Line call() throws Exception {
    long ms = player.timeRemaining();
    System.out.format("Timed task: %s attempting to make a play.%n", name);

//    Board board = this.board.clone();

    //player.restartClock();
    //Line answer = player.makePlay(board, oppPlay, ms);
    //player.stopClock();
    Line answer = doMakePlay(ms);

    ms = player.timeRemaining();
    System.out.format("Timed task: %s made play = %s, %s ms remaining.%n", name, answer, ms);
    return answer;
  }

  // This is a horrible hack to allow the player to use the Board and Square
  // ADTs without corrupting the boardDisplay.
  //
  Line doMakePlay(long ms) {
    Util.uiOff();
    player.restartClock();
    Line line = player.makePlay(board, oppPlay, ms);
    player.stopClock();
    Util.uiOn();
    return line;
  }

}

///////////////////////////////////////////////////////////////////////
//
// MATCH MODE
//
// This is the action listener for the Start button in MATCH mode.
// This is cribbed from playOneGame. Turns out that we couldn't call
// playOneGame repeatedly in a loop because the service threads were
// out of synch so we've basically copied-and-pasted and modified
// the code for playOneGame.
//
  public void playMatch() {

    this.stop = false;
    this.oppPlay = null;
    Score wins  = new ScoreC(0, 0);                    // in the match
    score = new ScoreC(0, 0);                          // boxes in the game
    final int games = controlDisplay.getGames();
    game = 1;

    controlDisplay.setStatus("Playing " + games + " games.");
    System.out.format("%nplayMatch: %d games, pace = %3.2f.%n", games, this.pace);

    // Create a couple of files who's names relate the player # to the
    // team name. But why? 
    //
    File out1 = new File("player1Is" + player1.teamName());
    File out2 = new File("player2Is" + player2.teamName());
    try {
      out1.createNewFile();
      out2.createNewFile();
    }
    catch (Exception e) {}

    ControlPanel cp = controlDisplay.getControlPanel();

    ProgressBar
      p1Progress = cp.getPlayer1PB(),
      p2Progress = cp.getPlayer2PB();

    // The time for a match is the time for a single game * the number of games.
    //
    long timeout = getTimeout()*25;  // ms excel
    System.out.format("playMatch: setting timeout = %d ms.%n", timeout);

    // The timed players here are either atBat or onDeck.
    //
    TimedPlayer
      player1 = new TimedPlayerC(this.player1, timeout, p1Progress),
      player2 = new TimedPlayerC(this.player2, timeout, p2Progress);

    // The following puts one player atBat and the other onDeck.
    //
    Players players = new Players(player1, player2);

    // The following service handler is the main driver of the MATCH.
    // It is called whenever a player executed in a non-IU blocking
    // service thread choses a Line within their timeout period.
    //
    this.service = new PlayService(players, this.board, this.oppPlay);

    service.setOnSucceeded( t -> {

      boolean filledASquare;

      // The fact that we're in the OnSucceeded handler means that the player
      // atBat chose a line within the allocated time (i.e., their clock
      // didn't expire and they didn't throw an exception). If either of these
      // had occurred we'd be in the OnFailed handler.
      //
      // At this point, we don't know if the chosen line is legal or whether
      // or not it claims any squares. If it's legal and it claimed a square
      // (or two), then the atBat player will get another crack at it if the
      // board isn't full. If the play was legal but didn't claim any squares
      // then the onDeck player is atBat and the atBat player is onDeck.
      //
      Player player = players.atBat.getPlayer();
      String name = player.teamName();

      // The line is chosen on a timeout-sensitive, non UI-blocking background
      // service thread.
      //
      Line line = (Line) t.getSource().getValue();
      this.oppPlay = line;

      System.out.format("in UI thread: %s chose line = %s.%n", name, line);

      // A line may claim 0, 1 or 2 squares. Board.markLine takes care of the
      // UI calls required to color the lines & captured squares if there are
      // any. The model logic is handled below. This is in a try-block because
      // Board.markLine throws an exception if the chosen line was already
      // marked.
      //
      try {
        this.claimedSquares = board.markLine(player, line);
      }
      catch (Exception e) {

        // We're probably here because the chosen line was already marked.
        // If so, the player that chose it loses the game.
        //
        // FIX THIS. LOSING BY VIOLATION SHOULD CONTINUE THE MATCH.
        //
        controlDisplay.setStatus(player.teamName() + " lost by rule violation.");
        showWinner(score, wins);
        playAnotherGame(service);
      }

      // The atBat player chose a legal line. Update the Score.
      //
      score.add(player, this.claimedSquares.size());
      controlDisplay.setGameScore(score);

      // Update the progress bar. We can't use the built-in progress update
      // gizmo for services. Or rather, we don't know how to use it in this
      // slightly non-standard situation where we have two progress bars.
      // We'll do the progress update by hand.
      //
      long timeRemaining = players.atBat.timeRemaining();
      double progress = 1.0 - timeRemaining / (double) this.timeout;
      System.out.format("Setting progress for %s to %f%n", player.teamName(), progress);
      players.atBat.getProgressBar().setProgress(progress);

      if (board.gameOver()) {
        showWinner(score, wins);
        if (game == games) {
          service.cancel();
          controlDisplay.setStatus("The match is over.");
          return;
        }
        // OK, the last game is over but the match isn't. Play another one.
        // As written, the player atBat of game n, is atBat for game n+1.
        //
        playAnotherGame(service);
      }
      else {
        filledASquare = !this.claimedSquares.isEmpty();

        if (!filledASquare) {
          players.swap();
          service.setAtBat(players.atBat, oppPlay);
          System.out.format("No squares filled, swapped players. ");
          System.out.format("Now atBat = %s, onDeck = %s.%n", players.atBat.teamName(), players.onDeck.teamName());
        }
        else
          System.out.format("In OnSucceeded: %s gets to go again.%n", name);

        if (!this.stop) {

          // The pace is set by the slider in the UI. Want to improve this by
          // including a slow start, fast middle, slow end by introducing a
          // sinusoidal coefficient:
          // 1.0 - Math.sin((elapsedTime / totalTime) * Math.PI)
          // I don't think this.delay is being update when the slider in the
          // UI is changed... FIGURE THIS OUT
          //
          Util.sleep(delay);

          if (service.getState() == Worker.State.READY)
            service.start(); // NOT CLEAR TO ME THIS ISN'T DEAD CODE
          else
            service.restart();
        }
      }
    });

    service.setOnFailed(t -> {
      System.out.format("In OnFailed.%n");
      String
        name = players.atBat.teamName(),
        message = "Match over. " + name + " threw an exception.";
      if (dbg.debug) dbg.println(message);
      controlDisplay.setStatus(message);
      service.cancel();
    });

    service.setOnCancelled(t -> {
      System.out.format("In OnCancelled.%n");
      if (dbg.debug) dbg.println("playOneGame: in OnCancelled handler.%n");
    });

    System.out.format("In playOneGame, starting on the PlayService.%n");
    service.start();
  }

  public void playAnotherGame(PlayService service) {
    this.oppPlay = null;
    this.score = new ScoreC(0, 0);
    this.game = this.game + 1;

    // Make a new board.
    //
    this.board = new BoardC(this, Util.N, Util.N);
    service.resetBoard(this.board);

    // And show it.
    //
    this.display = new Display(board, this, controlDisplay, width, height);
    stage.setScene(new Scene(display, width, height));

    System.out.format("%nPlaying game %d.%n", this.game);
    service.restart();
  }
}
