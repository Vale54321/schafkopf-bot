package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class WenzController extends GeierWenzController {
    public WenzController() {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> unterKarten = KartenUtil.getKartenBySymbol(kartenList, "u");

        kartenList.removeAll(unterKarten);

        this.trumpfKarten = new ArrayList<>(unterKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
