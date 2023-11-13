package org.example;

public class Karte {
    private String id;
    private String name;
    private String farbe;

    private String symbol;
    private int wert;
    Karte(String id, String name, String farbe, String symbol, int wert){
        this.id = id;
        this.name = name;
        this.farbe = farbe;
        this.symbol = symbol;
        this.wert = wert;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getFarbe(){
        return this.farbe;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
