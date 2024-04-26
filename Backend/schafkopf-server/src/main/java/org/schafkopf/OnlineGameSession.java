package org.schafkopf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.schafkopf.SchafkopfException.PlayerNotReadyException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.player.BotPlayer;
import org.schafkopf.player.OnlinePlayer;
import org.schafkopf.player.Player;

/**
 * The main entrypoint of the Application.
 */
public class OnlineGameSession extends BaseGameSession {

  private String serverName;

  private DedicatedServer dedicatedServer;

  /**
   * The main entrypoint of the Application.
   */
  public OnlineGameSession(String serverName, DedicatedServer dedicatedServer) {
    this.players = new ArrayList<>();
    this.dedicatedServer = dedicatedServer;
    this.serverName = serverName;
    logger.info(serverName + " created.");
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void addPlayer(Player player) {
    if (this.players.size() >= 4) {
      throw new RuntimeException("Game is full");
    }
    logger.info("Adding player to game: " + player);
    players.add(player);

    this.sendSessionInfo();
  }


  void startGame() throws PlayerNotReadyException {
    logger.info("Starting game: " + serverName + " with " + players.size() + " Onlineplayers");

    for (Player player : players) {
      if (!player.isReady()) {
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

    super.startGame(players);
    players.forEach(player -> player.resetReady());
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public void removePlayer(OnlinePlayer player) {
    logger.info("Removing player from game: " + player.getName());
    players.remove(player);

    if (this.getPlayerCount() == 0) {
      logger.info("No players left in game: " + serverName);
      if (spielThread != null) {
        spielThread.interrupt();
      }
      this.dedicatedServer.removeGameSession(this);
      return;
    }
    this.sendSessionInfo();
  }

  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    for (Player player : players) {
      player.sendMessage(message);
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
    for (Player player : players) {
      JsonObject playerObject = new JsonObject();
      playerObject.addProperty("playerName",
          player.getName()); // Assuming you have a method to get player name
      playerObject.addProperty("isReady",
          player.isReady()); // Assuming you have a method to check player readiness
      playersArray.add(playerObject);
      playerObject.addProperty("isBot",
          player instanceof BotPlayer);
    }
    for (int i = players.size(); i < 4; i++) {
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

  /**
   * The main entrypoint of the Application.
   */
  public int getPlayerCount() {
    int onlinePlayerCount = 0;
    for (Player player : players) {
      if (player instanceof OnlinePlayer) {
        onlinePlayerCount++;
      }
    }
    return onlinePlayerCount;
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
