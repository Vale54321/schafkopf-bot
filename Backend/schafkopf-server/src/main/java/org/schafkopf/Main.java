package org.schafkopf;

import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;

/** Creates an Instance of the Backend Server. */
public class Main {
  /** Creates an Instance of the Backend Server. */
  public static void main(String[] args) {

    System.out.println("Hello and welcome!");
    KartenListe testHand = KartenUtil.zieheZufallsHand(8);
    for (Karte karte : testHand.getKartenListe()) {
      System.out.println(karte.getName());
    }
  }
}