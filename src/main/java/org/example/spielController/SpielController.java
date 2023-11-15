package org.example.spielController;

import org.example.karte.KartenFarbe;
import org.example.karte.KartenListe;
import org.example.karte.KartenUtil;


public abstract class SpielController {
    protected static KartenListe trumpfKarten;
    protected static KartenListe farbKarten;

    public abstract int welcheKarteSpielich(KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten);

    public KartenListe getTrumpfKarten() {
        return trumpfKarten;
    }

    public KartenListe getFarbKarten() {
        return farbKarten;
    }

    public Karte welcheKarteSpielIch(List<Karte> gespielteKarten, List<Karte> eigeneKarten, List<Karte> tischKarten, List<Karte> unbekannteKarten) {
        return null;
    }
}
