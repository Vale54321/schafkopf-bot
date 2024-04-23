package org.schafkopf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.schafkopf.SchafkopfException.NotEnoughPlayersException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageOrigin;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.karte.Karte;
import org.schafkopf.player.OnlinePlayer;

/**
 * Class that represents one Frontend Connection.
 */
public class SchafkopfClientConnection extends WebSocketAdapter implements MessageSender {

  private final CountDownLatch connectionLatch;
  private final CountDownLatch closureLatch = new CountDownLatch(1);
  private DedicatedServer dedicatedServer;

  private GameSession gameSession;

  private Session session;

  private OnlinePlayer onlinePlayer;

  /**
   * Class that represents one Frontend Connection.
   */
  public SchafkopfClientConnection(DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
    this.connectionLatch = new CountDownLatch(1);
    System.out.println("new ClientConnection created.");
  }

  @Override
  public void onWebSocketConnect(Session session) {
    this.session = session;
    super.onWebSocketConnect(session);
    String clientIp = session.getRemoteAddress().toString();
    System.out.println("Endpoint connected from ip: " + clientIp);

    connectionLatch.countDown();
    dedicatedServer.addFrontendEndpoint(this);
    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.SERVER_CONNECTION_SUCCESSFUL));
  }

  @Override
  public void onWebSocketText(String jsonMessage) {
    super.onWebSocketText(jsonMessage);
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(jsonMessage, JsonObject.class);
    // Check if the origin is "frontend"
    String origin = jsonObject.get("origin").getAsString();
    if (SchafkopfMessageOrigin.BACKEND.toString().equals(origin)) {
      JsonObject message = jsonObject.getAsJsonObject("message");
      JsonObject content = message.getAsJsonObject("content");
      String messageType = message.get("message_type").getAsString();
      if ("HEARTBEAT_SYN".equals(messageType)) {

        sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.HEARTBEAT_ACK));
        return;
      } else if (SchafkopfMessageType.JOIN_GAME.toString().equals(messageType)) {

        gameSession = dedicatedServer.getCurrentGameSession();
        dedicatedServer.getCurrentGameSession().addPlayer(this);

      } else if ("START_GAME".equals(messageType)) {

        System.out.println("Received START_GAME message from " + session.getRemoteAddress() + ".");
        try {
          dedicatedServer.getCurrentGameSession().startGame();
        } catch (NotEnoughPlayersException e) {

          sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR,
              "Not enough players to start the game."));

        }

      } else if (SchafkopfMessageType.PLAYER_CARD.toString().equals(messageType)) {

        onlinePlayer.receiveCard(Karte.valueOf(content.get("card").getAsString()));

      } else if ("list_online_games".equals(messageType)) {
        System.out.println(
            "Received list_online_games message from " + session.getRemoteAddress() + ".");
      }

      System.out.println(
          "Received  message from Client " + session.getRemoteAddress() + " " + jsonMessage);
    }
  }


  public void setOnlinePlayer(OnlinePlayer onlinePlayer) {
    this.onlinePlayer = onlinePlayer;
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);

    dedicatedServer.removeFrontendEndpoint(this);

    System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    closureLatch.countDown();
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace(System.err);
  }

  /**
   * Send a message to the connected FrontEnd.
   */
  @Override
  public void sendMessage(SchafkopfBaseMessage message) {
    SchafkopfMessage schafkopfMessage = new SchafkopfMessage(
        SchafkopfMessageOrigin.DEDICATED_SERVER,
        message);
    System.out.println("Sending message to Client: " + schafkopfMessage.getMessageAsString());
    try {
      getRemote().sendString(schafkopfMessage.getMessageAsString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
