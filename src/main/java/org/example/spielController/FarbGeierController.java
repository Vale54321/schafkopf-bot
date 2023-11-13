package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class FarbGeierController extends SoloController{
    public FarbGeierController(String farbe) {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> oberKarten = KartenUtil.getKartenBySymbol(kartenList, "o");
        List<Karte> farbTrumpfKarten = KartenUtil.getKartenByFarbe(kartenList, farbe);
        farbTrumpfKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "o"));
        farbTrumpfKarten.addAll(oberKarten);
        kartenList.removeAll(farbTrumpfKarten);

        this.trumpfKarten = new ArrayList<>(farbTrumpfKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
