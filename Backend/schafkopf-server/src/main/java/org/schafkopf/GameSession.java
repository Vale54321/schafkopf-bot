package org.schafkopf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfException.PlayerNotReadyException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.OnlinePlayer;
import org.schafkopf.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main entrypoint of the Application.
 */
public class GameSession implements MessageSender {

  private static final Logger logger = LoggerFactory.getLogger(GameSession.class);
  private Schafkopf schafkopf;

  private List<SchafkopfClientConnection> clients;

  private Thread spielThread;
  private String serverName;

  private DedicatedServer dedicatedServer;

  /**
   * The main entrypoint of the Application.
   */
  public GameSession(String serverName, DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
    this.serverName = serverName;
    this.clients = new ArrayList<>();
    logger.info(serverName + " created.");
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void addPlayer(SchafkopfClientConnection client) {
    if (this.clients.size() >= 4) {
      throw new RuntimeException("Game is full");
    }
    logger.info("Adding player to game: " + client);
    clients.add(client);

    OnlinePlayer onlinePlayer = new OnlinePlayer(client, client.getName());
    client.setOnlinePlayer(onlinePlayer);
    this.sendSessionInfo();
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void removePlayer(SchafkopfClientConnection client) {
    logger.info("Removing player from game: " + client);
    clients.remove(client);

    if (clients.isEmpty()) {
      logger.info("No players left in game: " + serverName);
      if (spielThread != null) {
        spielThread.interrupt();
      }
      this.dedicatedServer.removeGameSession(this);
      return;
    }
    this.sendSessionInfo();
  }

  void startGame() throws PlayerNotReadyException {
    logger.info("Starting game: " + serverName + " with " + clients.size() + " players");
    List<Player> players = new ArrayList<>();

    for (SchafkopfClientConnection client : clients) {
      players.add(client.getOnlinePlayer());
    }
    for (int i = players.size(); i < 4; i++) {
      players.add(new BotPlayer("Bot " + i));
    }

    for (SchafkopfClientConnection client : clients) {
      if (!client.isReady()) {
        throw new PlayerNotReadyException();
      }
    }

    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.GAME_START_READY));

    //wait for 5 seconds
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    spielThread = new Thread(() -> {
      try {
        schafkopf = new Schafkopf(players.toArray(Player[]::new), this);
        schafkopf.startGame();
        clients.forEach(client -> client.resetReady());
      } catch (NotEnoughPlayersException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    spielThread.start();


  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    for (SchafkopfClientConnection client : clients) {
      client.sendMessage(message);
    }
  }

  /**
   * The main entrypoint of the Application.
   */
  public JsonObject getJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("serverName", serverName);
    jsonObject.addProperty("playerCount", getPlayerCount());

    // Create an array to hold player information
    JsonArray playersArray = new JsonArray();
    for (SchafkopfClientConnection client : clients) {
      JsonObject playerObject = new JsonObject();
      playerObject.addProperty("playerName",
          client.getName()); // Assuming you have a method to get player name
      playerObject.addProperty("isReady",
          client.isReady()); // Assuming you have a method to check player readiness
      playersArray.add(playerObject);
    }
    for (int i = clients.size(); i < 4; i++) {
      JsonObject playerObject = new JsonObject();
      playerObject.addProperty("playerName",
          "Bot " + i); // Assuming you have a method to get player name
      playerObject.addProperty("isReady",
          true);
      playerObject.addProperty("isBot",
          true);
      playersArray.add(playerObject);
    }
    jsonObject.add("players", playersArray);

    return jsonObject;
  }

  public String getServerName() {
    return serverName;
  }

  public int getPlayerCount() {
    return clients.size();
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void sendSessionInfo() {
    JsonObject messageObject2 = new JsonObject();
    messageObject2.add("game", this.getJson());
    sendMessage(
        new SchafkopfBaseMessage(SchafkopfMessageType.GET_ONLINE_GAME,
            messageObject2));
  }
}
