package org.example.spielController;
import org.example.Karte;
import org.example.KartenUtil;

import java.util.List;
import java.util.ArrayList;

public class SauSpielController extends SpielController{

    public SauSpielController(){
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> herzKarten = KartenUtil.getKartenByFarbe(kartenList, "herz");
        herzKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "u"));
        herzKarten.removeAll(KartenUtil.getKartenBySymbol(kartenList, "o"));

        herzKarten.addAll(KartenUtil.getKartenBySymbol(kartenList, "u"));
        herzKarten.addAll(KartenUtil.getKartenBySymbol(kartenList, "o"));

        kartenList.removeAll(herzKarten);

        this.trumpfKarten = new ArrayList<>(herzKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
