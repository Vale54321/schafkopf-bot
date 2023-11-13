package org.example.spielController;

import org.example.Karte;

import java.util.List;

public abstract class SpielController {
    protected List<Karte> trumpfKarten;
    protected List<Karte> farbKarten;



    public List<Karte> getTrumpfKarten() {
        return trumpfKarten;
    }

    public List<Karte> getFarbKarten() {
        return farbKarten;
    }
}
