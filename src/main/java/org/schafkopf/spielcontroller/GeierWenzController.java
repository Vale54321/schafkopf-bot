package org.schafkopf.spielcontroller;

import java.util.List;
import org.schafkopf.karte.Karte;

/**
 * SpielController that implements Logic of a Geier/Wenz Game.
 */
public class GeierWenzController extends SoloController {

  public GeierWenzController(int activePlayer) {
    super(activePlayer);
  }
  public Karte welcheKarteSpielich(
      List<Karte> gespielteKarten,
      List<Karte> meineHand,
      List<Karte> tischKarten,
      boolean istSpieler) {
    return null;
  }
}
