package org.example.spielController;

import org.example.karte.Karte;
import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class GeierController extends GeierWenzController {
    public GeierController() {
        KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
        KartenListe oberKarten = kartenList.getKarten(KartenSymbol.OBER);

        kartenList.removeKarten(oberKarten);

        this.trumpfKarten = new KartenListe(oberKarten);
        this.farbKarten = new KartenListe(kartenList);
    }
}
