package org.schafkopf;

import java.util.ArrayList;
import java.util.List;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.OnlinePlayer;
import org.schafkopf.player.Player;

/**
 * The main entrypoint of the Application.
 */
public class GameSession implements MessageSender {

  private Schafkopf schafkopf;
  private List<Player> player;

  private List<SchafkopfClientConnection> clients;

  private Thread spielThread;

  /**
   * The main entrypoint of the Application.
   */
  public GameSession() {
    player = new ArrayList<>();
    clients = new ArrayList<>();
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void addPlayer(SchafkopfClientConnection client) {
    if (this.player.size() >= 4) {
      throw new RuntimeException("Game is full");
    }
    System.out.println("Adding player to game: " + client);
    clients.add(client);

    OnlinePlayer onlinePlayer = new OnlinePlayer(client);

    this.player.add(onlinePlayer);

    client.setOnlinePlayer(onlinePlayer);
  }

  public Schafkopf getSchafkopf() {
    return schafkopf;
  }

  void startGame() throws NotEnoughPlayersException {
    int playerCount = this.player.size();
    System.out.println("Starting game with " + playerCount + " players");
    if (playerCount < 4) {
      for (int i = 0; i < 4 - playerCount; i++) {
        this.player.add(new BotPlayer());
      }
    }
    System.out.println("Starting game with, now: " + this.player.size() + " players");

    spielThread = new Thread(() -> {
      try {
        schafkopf = new Schafkopf(this.player.toArray(new Player[0]), this);
        schafkopf.startGame();
      } catch (NotEnoughPlayersException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    spielThread.start();

    //    schafkopf = new Schafkopf(this.player.toArray(new Player[0]), this);

  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    System.out.println("Sending message to Client: " + message);
    for (SchafkopfClientConnection client : clients) {
      client.sendMessage(message);
    }
  }
}
