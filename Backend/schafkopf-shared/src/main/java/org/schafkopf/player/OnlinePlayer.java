package org.schafkopf.player;

import com.google.gson.JsonObject;
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

  private KartenListe karten = new KartenListe();

  public OnlinePlayer(MessageSender messageSender, String name) {
    super(name);
    this.messageSender = messageSender;
  }

  /**
   * A Class that represents a list of Cards.
   */
  public void setAndSendPlayerCards(KartenListe karten) {
    karten.sort();
    this.karten = karten;

    sendPlayerCards();
  }

  @Override
  public Karte play(SpielController spiel, KartenListe tischKarten, KartenListe gespielteKarten)
      throws InterruptedException {
    sendPlayerCards();
    Karte spielKarte = null;

    // Send the message to request the card from the frontend
    messageSender.sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.GET_CARD_ONLINE_PLAYER));

    spielKarte = receivedCardQueue.take();

    this.karten.removeKarten(spielKarte);
    sendPlayerCards();
    System.out.println("Karte gespielt: " + spielKarte);
    return spielKarte;
  }

  private void sendPlayerCards() {
    JsonObject messageObject = new JsonObject();
    messageObject.add("cards", this.karten.getJson());

    messageSender.sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.ONLINE_PLAYER_HAND, messageObject));
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void receiveCard(Karte receivedCard) {
    System.out.println("Received Card before Queue: " + receivedCard.getName());
    receivedCardQueue.add(receivedCard);
  }
}
