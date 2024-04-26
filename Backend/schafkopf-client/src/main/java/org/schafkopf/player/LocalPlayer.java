package org.schafkopf.player;

import org.schafkopf.MessageSender;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.cardreader.CardReader;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that plays in real life.
 */
public class LocalPlayer extends Player {

  private final CardReader cardReader;

  public LocalPlayer(CardReader cardReader, MessageSender messageSender) {
    super("Local Player", messageSender);
    this.cardReader = cardReader;
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten) {
    return wartetAufKarte();
  }

  @Override
  public void resetReady() {
    // Not needed
  }

  /**
   * Waits for a Card and returns a Karte Object.
   */
  private Karte wartetAufKarte() {
    String uid = null;
    System.out.println("Starte Warten auf Karte");
    try {
      uid = cardReader.waitForCardScan();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Karte karte = KartenUtil.getIdOfUid(uid);

    if (karte == null) {
      System.out.println("Ungültige Karte");
      return wartetAufKarte();
    }
    System.out.println("Karte gescannt: " + karte.getName());
    System.out.println("Beende Warten auf Karte");
    return karte;
  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    System.out.println("LocalPlayer: " + message);
  }
}
