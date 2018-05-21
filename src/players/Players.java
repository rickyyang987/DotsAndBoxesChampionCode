package players;

import util.*;
import javafx.concurrent.Service;
import javafx.scene.control.ProgressBar;

public class Players {

  public TimedPlayer atBat, onDeck;
  private DBG dbg;

  public Players(TimedPlayer a, TimedPlayer b) {

    this.dbg = new DBG(DBG.CONTROL, "Players");

    // Flip a coin to see who goes first. HEADS UP service not in SCOPE!!!
    //
    if (Math.random() < .5) {
      this.atBat = a;
      this.onDeck = b;
    }
    else {
      this.atBat = b;
      this.onDeck = a;
    }
    if(dbg.debug) {
      dbg.println(this.atBat.teamName() + " playing first.");
      dbg.println(this.onDeck.teamName() + " playing second.");
    }
  }

  public void swap() {
    TimedPlayer temp = this.atBat;
    this.atBat = this.onDeck;
    this.onDeck = temp;
  }
}
