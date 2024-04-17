package org.schafkopf;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;

/** Creates an Instance of the Backend Server. */
public class Main {
  /** Creates an Instance of the Backend Server. */
  public static void main(String[] args) {
    // Press Alt+Eingabe with your caret at the highlighted text to see how
    // IntelliJ IDEA suggests fixing it.
    System.out.printf("Hello and welcome!");
    KartenListe testHand = KartenUtil.zieheZufallsHand(8);
    // Press Umschalt+F10 or click the green arrow button in the gutter to run the code.
    for (Karte karte : testHand.getKartenListe()) {
      System.out.println(karte.getName());
    }
  }
}