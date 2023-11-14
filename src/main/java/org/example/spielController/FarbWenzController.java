package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class FarbWenzController extends SoloController{
    public FarbWenzController(String farbe) {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> unterKarten = KartenUtil.getKartenBySymbol(kartenList, "u");
        List<Karte> farbTrumpfKarten = KartenUtil.getKartenByFarbe(kartenList, farbe);
        farbTrumpfKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "u"));
        farbTrumpfKarten.addAll(unterKarten);
        kartenList.removeAll(farbTrumpfKarten);

        this.trumpfKarten = new ArrayList<>(farbTrumpfKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }

    public Karte welcheKarteSpielich(List<Karte> gespielteKarten, List<Karte> meineHand, List<Karte> tischKarten, boolean istSpieler){
        return null;
    }
}
