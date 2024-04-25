package org.schafkopf.player;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Class that represents one Player of the game.
 */
public abstract class Player {

  private String name;

  protected Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract Karte play(
      SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten)
      throws InterruptedException;
}
