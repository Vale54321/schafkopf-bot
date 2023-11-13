package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

public class GeierController extends GeierWenzController {
    public GeierController() {
        List<Karte> kartenList = KartenUtil.initializeSchafKopfCardDeck();
        List<Karte> oberKarten = KartenUtil.getKartenBySymbol(kartenList, "o");

        kartenList.removeAll(oberKarten);

        this.trumpfKarten = new ArrayList<>(oberKarten);
        this.farbKarten = new ArrayList<>(kartenList);
    }
}
