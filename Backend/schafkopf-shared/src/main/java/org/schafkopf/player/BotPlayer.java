package org.schafkopf.player;

import org.schafkopf.MessageSender;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.karte.KartenUtil;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that represents the Bot.
 */
public class BotPlayer extends Player {

  private KartenListe eigeneKarten;
  private KartenListe unbekannteKarten = KartenUtil.initializeSchafKopfCardDeck();

  /**
   * Constructor for the BotPlayer.
   */
  public BotPlayer(String name) {
    super(name, new MessageSender() {
      @Override
      public void sendMessage(SchafkopfBaseMessage message) {
        System.out.println("BotPlayer: " + message);
      }
    });
    this.setReady(true);
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten) {

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Karte card = spiel.welcheKarteSpielIch(true, gespielteKarten, eigeneKarten, tischKarten);

    eigeneKarten.removeKarten(card);

    System.out.println("Eigene Karte legen");
    return card;
  }

  /**
   * Set the Cards of the Player.
   */
  public void setCards(KartenListe cards) {
    System.out.println("Eigene Karte setzen");
    cards.sort();
    this.eigeneKarten = cards;
    this.unbekannteKarten = KartenUtil.initializeSchafKopfCardDeck();
    this.unbekannteKarten.removeKarten(eigeneKarten);
    System.out.println("Eigene Karte fertig");
  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {

  }

  @Override
  public void resetReady() {
    // Not needed
  }
}
