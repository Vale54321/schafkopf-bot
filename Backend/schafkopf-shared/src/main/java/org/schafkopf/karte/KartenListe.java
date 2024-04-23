package org.schafkopf.karte;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Class that represents a list of Cards.
 */
public class KartenListe {

  private List<Karte> kartenListe;

  public KartenListe() {
    this.kartenListe = new ArrayList<>();
  }

  public KartenListe(KartenListe liste) {
    this.kartenListe = new ArrayList<>(liste.getKartenListe());
  }

  public List<Karte> getKartenListe() {
    return this.kartenListe;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public void sort() {
    KartenListe completeDeck = KartenUtil.initializeSchafKopfCardDeck();
    completeDeck.removeKarten(this);

    KartenListe completeDeck2 = KartenUtil.initializeSchafKopfCardDeck();
    completeDeck2.removeKarten(completeDeck);

    this.kartenListe = completeDeck2.getKartenListe();
  }

  public void shuffle() {
    Collections.shuffle(this.kartenListe);
  }

  /**
   * A Class that represents a list of Cards.
   */
  public void addKarten(Karte karte) {
    if (!this.containsKarte(karte)) {
      this.kartenListe.add(karte);
      return;
    }
    throw new RuntimeException("Karte bereits vorhanden: " + karte.getName());
  }

  // methoden zum hinzufügen von karten

  /**
   * A Class that represents a list of Cards.
   */
  public void addKarten(KartenListe karten) {
    for (Karte karte : karten.getKartenListe()) {
      this.addKarten(karte);
    }
  }

  /**
   * A Class that represents a list of Cards.
   */
  public KartenListe removeKarten(KartenListe karten) {
    KartenListe result = new KartenListe();
    for (Karte karteWeg : karten.getKartenListe()) {
      for (Karte karte : this.kartenListe) {
        if (karte.getId().equals(karteWeg.getId())) {
          result.addKarten(karte);
          break;
        }
      }
    }
    this.kartenListe.removeAll(result.getKartenListe());
    return result;
  }

  // methoden zum entfernen von karten

  /**
   * A Class that represents a list of Cards.
   */
  public KartenListe removeKarten(KartenFarbe farbe) {
    KartenListe result = new KartenListe();
    for (Karte karte : this.kartenListe) {
      if (karte.getFarbe().equals(farbe)) {
        result.addKarten(karte);
      }
    }
    this.kartenListe.removeAll(result.getKartenListe());
    return result;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public KartenListe removeKarten(KartenSymbol symbol) {
    KartenListe result = new KartenListe();
    for (Karte karte : this.kartenListe) {
      if (karte.getSymbol().equals(symbol)) {
        result.addKarten(karte);
      }
    }
    this.kartenListe.removeAll(result.getKartenListe());
    return result;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public Karte removeKarten(Karte karteToRemove) {
    for (Karte karte : this.kartenListe) {
      if (karte.getId().equals(karteToRemove.getId())) {
        this.kartenListe.remove(karte);
        return karte;
      }
    }
    return null;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public Karte removeKarten(String idToRemove) {
    for (Karte karte : this.kartenListe) {
      if (karte.getId().equals(idToRemove)) {
        this.kartenListe.remove(karte);
        return karte;
      }
    }
    return null;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public boolean containsKarte(Karte karte) {
    for (Karte karteInListe : this.kartenListe) {
      if (karteInListe.getId().equals(karte.getId())) {
        return true;
      }
    }
    return false;
  }

  // get Karten

  /**
   * A Class that represents a list of Cards.
   */
  public KartenListe getKarten(KartenFarbe farbe) {
    KartenListe result = new KartenListe();
    for (Karte karte : this.kartenListe) {
      if (karte.getFarbe().equals(farbe)) {
        result.addKarten(karte);
      }
    }
    return result;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public KartenListe getKarten(KartenSymbol symbol) {
    KartenListe result = new KartenListe();
    for (Karte karte : this.kartenListe) {
      if (karte.getSymbol().equals(symbol)) {
        result.addKarten(karte);
      }
    }
    return result;
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public Karte getKarten(KartenSymbol symbol, KartenFarbe farbe) {
    KartenListe result = new KartenListe();
    for (Karte karte : this.kartenListe) {
      if (karte.getSymbol().equals(symbol)) {
        result.addKarten(karte);
      }
    }
    for (Karte karte : result.kartenListe) {
      if (karte.getFarbe().equals(farbe)) {
        result.addKarten(karte);
      }
    }
    return result.getByIndex(0);
  }

  /**
   * A Class that represents a list of Cards.
   */
  public JsonElement getJson() {
    Gson gson = new Gson();

    return gson.toJsonTree(this.kartenListe);
  }

  public boolean isEmpty() {
    return this.kartenListe.isEmpty();
  }

  public Karte getLast() {
    return this.kartenListe.getLast();
  }

  public Karte getByIndex(int index) {
    return this.kartenListe.get(index);
  }

  public int size() {
    return this.kartenListe.size();
  }

  /**
   * A Class that represents a list of Cards.
   */
  public int indexOf(Karte karte) {
    for (Karte karteInListe : this.kartenListe) {
      if (karteInListe.getId().equals(karte.getId())) {
        return this.kartenListe.indexOf(karteInListe);
      }
    }
    return -1;
  }

  public void clear() {
    this.kartenListe.clear();
  }
}
