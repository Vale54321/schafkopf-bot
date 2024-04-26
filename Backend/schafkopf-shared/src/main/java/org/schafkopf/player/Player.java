package org.schafkopf.player;

import org.schafkopf.MessageSender;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Class that represents one Player of the game.
 */
public abstract class Player implements MessageSender {

  protected MessageSender messageSender;
  private boolean ready = false;
  private String name;

  protected Player(String name, MessageSender messageSender) {
    this.messageSender = messageSender;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public abstract Karte play(
      SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten)
      throws InterruptedException;

  public abstract void resetReady();

  public void setReady(boolean ready) {
    this.ready = ready;
  }

  public boolean isReady() {
    return ready;
  }
}
