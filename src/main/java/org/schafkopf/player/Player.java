package org.schafkopf.player;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.spielcontroller.SpielController;

public abstract class Player {
  public abstract Karte play(SpielController spiel, KartenListe tischKarten);
}
