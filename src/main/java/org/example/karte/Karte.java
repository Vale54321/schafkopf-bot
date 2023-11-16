package org.example.karte;

public class Karte {
    private String id;
    private String name;
    private KartenFarbe farbe;
    private KartenSymbol symbol;
    private int punkte;

    Karte(KartenFarbe farbe, KartenSymbol symbol){
        this.id = farbe.getDisplayName().toLowerCase() + "_" + symbol.getDisplayName().toLowerCase();
        this.name = farbe.getDisplayName() + " " + symbol.getDisplayName();
        this.farbe = farbe;
        this.symbol = symbol;
        this.punkte = symbol.getValue();
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public KartenFarbe getFarbe(){
        return this.farbe;
    }

    public KartenSymbol getSymbol() {
        return this.symbol;
    }

    public int getPunkte() {
        return this.punkte;
    }
}
