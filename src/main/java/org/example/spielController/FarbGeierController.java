package org.example.spielController;

import org.example.karte.*;

import java.util.ArrayList;
import java.util.List;

public class FarbGeierController extends SoloController{
    public FarbGeierController(KartenFarbe farbe) {
        KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
        KartenListe oberKarten = kartenList.getKarten(KartenSymbol.OBER);
        KartenListe farbTrumpfKarten = kartenList.getKarten(farbe);
        farbTrumpfKarten.removeKarten(KartenSymbol.OBER);
        farbTrumpfKarten.addKarten(oberKarten);
        kartenList.removeKarten(farbTrumpfKarten);

        this.trumpfKarten = new KartenListe(farbTrumpfKarten);
        this.farbKarten = new KartenListe(kartenList);
    }
}
