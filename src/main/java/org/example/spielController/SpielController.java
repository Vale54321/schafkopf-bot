package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.ArrayList;
import java.util.List;

import static org.example.KartenUtil.removeKarten;

public abstract class SpielController {
    protected static List<Karte> trumpfKarten;
    protected static List<Karte> farbKarten;

    public abstract int welcheKarteSpielich(List<Karte> gespielteKarten, List<Karte> meineHand, List<Karte> tischKarten);

    public List<Karte> getTrumpfKarten() {
        return trumpfKarten;
    }

    public List<Karte> getFarbKarten() {
        return farbKarten;
    }

    public static int farbeZugeben(List<Karte> meineHand, String farbe, int mode){
        List<Karte> farbKarten = KartenUtil.getKartenByFarbe(meineHand, farbe);
        removeKarten(farbKarten, trumpfKarten);
        if(farbKarten.size() == 1){
            return meineHand.indexOf(farbKarten.get(0));
        }
        if(farbKarten.size() > 1){
            switch (mode){
                case 0:
                    return 0;
                case 1:
                    return meineHand.indexOf(farbKarten.getLast());
                case 2:
                    return meineHand.indexOf(farbKarten.getLast());
                default:
                    return 0;
            }
        }
        if(farbKarten.isEmpty()){
            switch (mode){
                case 0:
                    return 0;
                case 1:
                    return 0;
                case 2:
                    return meineHand.size()-1;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static void sortiereKarten(List<Karte> karten) {
        List<Karte> kartenReihenfolge = new ArrayList<>(farbKarten);
        kartenReihenfolge.addAll(trumpfKarten);

        List<Karte> kartenListe = KartenUtil.initializeSchafKopfCardDeck();

        removeKarten(kartenListe, karten);
        removeKarten(kartenReihenfolge, kartenListe);

        karten.clear();
        karten.addAll(kartenReihenfolge);
    }

    public static int welcheKarteSticht(List<Karte> karten){
        List<Karte> kartenNew = new ArrayList<>(karten);
        sortiereKarten(kartenNew);

        int i = karten.indexOf(kartenNew.getLast());

        System.out.println("karte sticht: " + i);
        return i;
    }
}
