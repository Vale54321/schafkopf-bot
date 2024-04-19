package org.schafkopf.player;

import org.schafkopf.BackendServer;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that plays in real life.
 */
public class LocalPlayer extends Player {

  private final BackendServer server;

  public LocalPlayer(BackendServer server) {
    this.server = server;
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten) {
    return wartetAufKarte();
  }

  /** Waits for a Card and returns a Karte Object. */
  private Karte wartetAufKarte() {
    String uid = null;
    System.out.println("Starte Warten auf Karte");
    try {
      uid = server.waitForCardScan();
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
}
