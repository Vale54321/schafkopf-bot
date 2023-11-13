package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class FarbSoloController extends SoloController {
    public FarbSoloController(String farbe) {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> unterKarten = KartenUtil.getKartenBySymbol(kartenList, "u");

        List<Karte> farbTrumpfKarten = KartenUtil.getKartenByFarbe(kartenList, farbe);
        farbTrumpfKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "u"));
        farbTrumpfKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "o"));
        farbTrumpfKarten.addAll(KartenUtil.getKartenBySymbol(kartenList, "u"));
        farbTrumpfKarten.addAll(KartenUtil.getKartenBySymbol(kartenList, "o"));

        kartenList.removeAll(farbTrumpfKarten);

        this.trumpfKarten = new ArrayList<>(farbTrumpfKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
