package org.example.spielController;

import org.example.Karte;
import org.example.KartenUtil;

import java.util.List;

public abstract class SpielController {
    protected List<Karte> trumpfKarten;
    protected List<Karte> farbKarten;

    public abstract Karte welcheKarteSpielich(List<Karte> gespielteKarten, List<Karte> meineHand, List<Karte> tischKarten);

    public List<Karte> getTrumpfKarten() {
        return trumpfKarten;
    }

    public List<Karte> getFarbKarten() {
        return farbKarten;
    }

    public Karte farbeZugeben(List<Karte> meineHand, String farbe, int mode){
        List<Karte> farbKarten = KartenUtil.getKartenByFarbe(meineHand, farbe);
        KartenUtil.removeKarten(farbKarten, trumpfKarten);
        if(farbKarten.size() == 1){
            return farbKarten.get(0);
        }
        if(farbKarten.size() > 1){
            switch (mode){
                case 0:
                    return farbKarten.getFirst();
                case 1:
                    return farbKarten.getLast();
                case 2:
                    return farbKarten.getLast();
                default:
                    return null;
            }
        }
        if(farbKarten.isEmpty()){
            switch (mode){
                case 0:
                    return meineHand.getFirst();
                case 1:
                    return meineHand.getFirst();
                case 2:
                    return meineHand.getLast();
                default:
                    return null;
            }
        }
        return null;
    }

    public int welcheKarteSticht(List<Karte> karten){
        return 0;
    }
}
