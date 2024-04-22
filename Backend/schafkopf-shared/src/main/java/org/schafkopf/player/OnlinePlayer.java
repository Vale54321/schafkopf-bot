package org.schafkopf.player;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.schafkopf.MessageSender;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.karte.Karte;
import org.schafkopf.karte.KartenListe;
import org.schafkopf.spielcontroller.SpielController;

/**
 * Player that plays in real life.
 */
public class OnlinePlayer extends Player {

  private final BlockingQueue<Karte> cardQueue = new LinkedBlockingQueue<>();
  private final MessageSender messageSender;

  public OnlinePlayer(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten) {
    Karte spielKarte = null;
    try {
      System.out.println("Waiting for card in play method...");
      messageSender.sendMessage(
          new SchafkopfBaseMessage(SchafkopfMessageType.GET_CARD_ONLINE_PLAYER));
      spielKarte = cardQueue.take();
      System.out.println("Card received in play method: " + spielKarte);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Karte gespielt: " + spielKarte);
    return spielKarte;
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void receiveMessage(String message) {
    System.out.println("Received Card before Queue: " + message);
    Karte receivedCard = Karte.valueOf(message);
    cardQueue.offer(receivedCard);
    System.out.println("Received Card after Queue: " + message);
  }
}
