package org.schafkopf.spielcontroller;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;

/**
 * SpielController that implements Logic of a Geier/Wenz Game.
 */
public class GeierWenzController extends SoloController {

  public GeierWenzController(int activePlayer) {
    super(activePlayer);
  }

  @Override
  public Karte welcheKarteSpielIch(boolean istSpieler, KartenListe gespielteKarten,
      KartenListe meineHand, KartenListe tischKarten) {
    return null;
  }
}
