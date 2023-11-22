package org.example.spielController;

import org.example.karte.KartenListe;

public abstract class SoloController extends SpielController {
  public int welcheKarteSpielIch(
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten) {
    return 0;
  }
}
