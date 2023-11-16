package org.example.spielController;

import org.example.karte.*;

import java.util.ArrayList;
import java.util.List;

public abstract class StandardController extends SpielController{

    StandardController(){
        KartenListe kartenList = KartenUtil.initializeSchafKopfCardDeck();
        KartenListe herzKarten = kartenList.getKarten(KartenFarbe.HERZ);
        herzKarten.removeKarten(KartenSymbol.UNTER);
        herzKarten.removeKarten(KartenSymbol.OBER);

        herzKarten.addKarten(kartenList.getKarten(KartenSymbol.UNTER));
        herzKarten.addKarten(kartenList.getKarten(KartenSymbol.OBER));

        kartenList.removeKarten(herzKarten);

        this.trumpfKarten = new KartenListe(herzKarten);
        this.farbKarten = new KartenListe(kartenList);
    }
    public abstract int welcheKarteSpielich(KartenListe gespielteKarten, KartenListe meineHand, KartenListe tischKarten);
}
