package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class GeierSpielController extends SpielController {
    public GeierSpielController(String farbe) {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> oberKarten = KartenUtil.getKartenBySymbol(kartenList, "o");
        List<Karte> farbTrumpfKarten = KartenUtil.getKartenByFarbe(kartenList, farbe);
        farbTrumpfKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "o"));
        farbTrumpfKarten.addAll(oberKarten);
        kartenList.removeAll(farbTrumpfKarten);

        this.trumpfKarten = new ArrayList<>(farbTrumpfKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }

    public GeierSpielController() {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> oberKarten = KartenUtil.getKartenBySymbol(kartenList, "o");

        kartenList.removeAll(oberKarten);

        this.trumpfKarten = new ArrayList<>(oberKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
