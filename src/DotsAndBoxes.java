import util.*;
import controller.*;
import ui.BoardDisplay;
import ui.ControlDisplay;
import board.*;
import players.*;
import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

public class DotsAndBoxes extends Application {

  public static Scene makeScene(Stage stage, Mode mode, int width, int height) {
    // stage is passed to the controller so that the controller can perform
    // a stage.setScene in response to a reset.
    //
    Controller controller = new ControllerC(stage, mode, width, height);
    Scene scene = new Scene(controller.getDisplay(), width, height);
    return scene;
  }

  @Override
  public void start(Stage stage) throws Exception {
    int
      height = Util.DEFAULT_DISPLAY_HEIGHT,
      width =  (int) (height * Util.ASPECT_RATIO);

    Scene scene = makeScene(stage, Mode.INTERACTIVE, width, height);
    stage.setScene(scene);

    stage.setTitle("CSCI 1102 Dots & Boxes Tournament");
    stage.setOnCloseRequest( e -> {
      System.out.format("Closing Dots & Boxes%n");
      try { stop(); } catch (Exception exn) { }
    });

    //Display the window.
    //
    stage.show();
  }

  //public static void main(String[] args) {
  //  Application.launch(args);
  //}

  public static void main(String[] args) {

    // As of now, one optional command line argument: an integer N
    // specifying the board dimensions. The default is 4.
    //
    if (args.length > 0)
      Util.N = Integer.parseInt(args[0]);

    Application.launch(args);
  }
}
