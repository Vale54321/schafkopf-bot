package org.example.spielController;

import org.example.karte.Karte;
import org.example.karte.KartenListe;

import java.util.List;

public abstract class SoloController extends SpielController {
  public int welcheKarteSpielIch(
      KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten) {
    return 0;
  }
}
