package org.schafkopf;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.spielcontroller.SpielController;

/** The main class that controlls the game flow. */
public class Spielablauf {
  private final KartenListe gespielteKarten;
  private final KartenListe eigeneKarten;
  private final KartenListe tischKarten;
  private final KartenListe unbekannteKarten;
  private final SpielController spiel;
  private final boolean aktiverSpieler; // ob der bot Spieler ist oder nicht
  Schafkopf schafkopf;
  private int startSpieler;
  private int gemachteStiche;

  Spielablauf(
      Schafkopf schafkopf,
      SpielController spiel,
      KartenListe hand,
      int startSpieler,
      boolean aktiverSpieler) {
    this.schafkopf = schafkopf;
    this.spiel = spiel;
    this.startSpieler = startSpieler;
    this.aktiverSpieler = aktiverSpieler;
    this.eigeneKarten = hand;
    KartenListe alleKarten = KartenUtil.initializeSchafKopfCardDeck();
    this.unbekannteKarten = new KartenListe(alleKarten);
    this.unbekannteKarten.removeKarten(this.eigeneKarten);
    gespielteKarten = new KartenListe();
    tischKarten = new KartenListe();
    gemachteStiche = 0;
    try {
      einStich();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Method to Handle flow of one Game.
   */
  public void einStich() throws InterruptedException {
    System.out.println();
    System.out.println();
    System.out.println("Starte Stiche");
    while (gemachteStiche < 8) {
      schafkopf.getServer().sendMessageToAllFrontendEndpoints("gespielte Karten:");
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(eigeneKarten.getJson());
      Thread.sleep(10000);
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
      Thread.sleep(10000);
      System.out.println("Stich: " + gemachteStiche);
      int i = startSpieler;
      while (i % 4 != 0) {
        System.out.println("warte auf Karte von Spieler " + i);
        Karte gelegteKarte = schafkopf.wartetAufKarte(); // warten auf Karteneingabe
        System.out.println("erwartete Karte angekommen");
        tischKarten.addKarten(gelegteKarte);
        //                unbekannteKarten.removeKarten(gelegteKarte);
        i++;
      }
      System.out.println("Eigene Karte legen");
      // eigeneKarteLegen
      schafkopf.getServer().sendMessageToAllFrontendEndpoints(tischKarten.getJson());
      int eigeneGelegteKarte =
          spiel.welcheKarteSpielIch(
              gespielteKarten,
              eigeneKarten,
              tischKarten); // Welche KArte soll der Bot spielen, Achtung! unterschiedliche Länge
      // der Listen pro Anfrage, aktiverSpieler hinzufügen
      schafkopf
          .getServer()
          .sendMessageToAllFrontendEndpoints(eigeneKarten.getByIndex(eigeneGelegteKarte).getJson());
      tischKarten.addKarten(eigeneKarten.getByIndex(eigeneGelegteKarte));
      eigeneKarten.removeKarten(eigeneKarten.getByIndex(eigeneGelegteKarte));

      while (tischKarten.size() < 4) { // tischkarten zählen
        System.out.println("warte auf Karte von Spieler " + tischKarten.size());
        Karte gelegteKarte = schafkopf.wartetAufKarte(); // warten auf Karteneingabe
        System.out.println("erwartete Karte angekommen");
        tischKarten.addKarten(gelegteKarte);
        //                unbekannteKarten.removeKarten(gelegteKarte);
      }
      System.out.println("Stiche ende");
      System.out.println();
      System.out.println();
      Thread.sleep(2000);
      //            int stichSpieler = spiel.werHatGestochen(tischKarten); //returns int(0,1,2,3)
      int stichSpieler = 0;
      startSpieler = startSpieler + stichSpieler;
      if (startSpieler > 3) {
        startSpieler = startSpieler % 4;
      }

      gespielteKarten.addKarten(tischKarten);
      tischKarten.clear();

      gemachteStiche++;
    }
    schafkopf.getServer().sendMessageToAllFrontendEndpoints(gespielteKarten.getJson());
  }
}
