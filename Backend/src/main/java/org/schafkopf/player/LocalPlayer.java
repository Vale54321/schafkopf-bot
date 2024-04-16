package org.schafkopf.player;

import org.schafkopf.Schafkopf;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that plays in real life.
 */
public class LocalPlayer extends Player {

  private final Schafkopf schafkopf;

  public LocalPlayer(Schafkopf schafkopf) {
    this.schafkopf = schafkopf;
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten) {
    return schafkopf.wartetAufKarte();
  }
}
