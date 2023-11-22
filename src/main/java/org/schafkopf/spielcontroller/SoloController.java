package org.schafkopf.spielcontroller;

import org.schafkopf.karte.KartenListe;

/**
 * abstract Class that represents Logic of a Solo like Game.
 */
public abstract class SoloController extends SpielController {

  SoloController(int activePlayer) {
    super(activePlayer);
  }
  public int welcheKarteSpielIch(
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten) {
    return 0;
  }
}
