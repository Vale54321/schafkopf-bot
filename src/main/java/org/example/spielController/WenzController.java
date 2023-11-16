package org.example.spielController;

import org.example.karte.Karte;
import org.example.karte.KartenListe;
import org.example.karte.KartenSymbol;
import org.example.karte.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class WenzController extends GeierWenzController {
    public WenzController() {
        KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
        KartenListe unterKarten = kartenList.getKarten(KartenSymbol.UNTER);

        kartenList.removeKarten(unterKarten);

        this.trumpfKarten = new KartenListe(unterKarten);
        this.farbKarten = new KartenListe(kartenList);
    }
}
