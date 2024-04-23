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

  private final MessageSender messageSender;
  private final BlockingQueue<Karte> receivedCardQueue = new LinkedBlockingQueue<>();

  public OnlinePlayer(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten)
      throws InterruptedException {
    Karte spielKarte = null;

    // Send the message to request the card from the frontend
    messageSender.sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.GET_CARD_ONLINE_PLAYER));

    spielKarte = receivedCardQueue.take();

    System.out.println("Karte gespielt: " + spielKarte);
    return spielKarte;
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void receiveCard(Karte receivedCard) {
    System.out.println("Received Card before Queue: " + receivedCard.getName());
    receivedCardQueue.add(receivedCard);
  }
}
