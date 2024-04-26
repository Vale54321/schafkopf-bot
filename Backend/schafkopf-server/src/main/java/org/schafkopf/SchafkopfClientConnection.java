package org.schafkopf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.schafkopf.SchafkopfException.NoGameSessionException;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageOrigin;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;
import org.schafkopf.karte.Karte;
import org.schafkopf.player.OnlinePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that represents one Frontend Connection.
 */
public class SchafkopfClientConnection extends WebSocketAdapter implements MessageSender {

  private static final Logger logger = LoggerFactory.getLogger(SchafkopfClientConnection.class);
  private final CountDownLatch connectionLatch;
  private final CountDownLatch closureLatch = new CountDownLatch(1);
  private DedicatedServer dedicatedServer;

  private OnlineGameSession onlineGameSession;

  private OnlinePlayer onlinePlayer;

  /**
   * Class that represents one Frontend Connection.
   */
  public SchafkopfClientConnection(DedicatedServer dedicatedServer) {
    this.dedicatedServer = dedicatedServer;
    this.connectionLatch = new CountDownLatch(1);
    this.onlinePlayer = new OnlinePlayer(this, "DefaultName");
  }

  @Override
  public void onWebSocketConnect(Session session) {
    super.onWebSocketConnect(session);
    String clientIp = session.getRemoteAddress().toString();
    logger.info("Endpoint connected from ip: " + clientIp);
    connectionLatch.countDown();
    dedicatedServer.addFrontendEndpoint(this);
    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.SERVER_CONNECTION_SUCCESSFUL));
  }

  @Override
  public void onWebSocketText(String jsonMessage) {
    super.onWebSocketText(jsonMessage);
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(jsonMessage, JsonObject.class);

    // Check if the origin is "backend"
    String origin = jsonObject.get("origin").getAsString();
    if (!SchafkopfMessageOrigin.BACKEND.toString().equals(origin)) {
      return;
    }

    JsonObject message = jsonObject.getAsJsonObject("message");
    JsonObject content = message.getAsJsonObject("content");
    String messageType = message.get("message_type").getAsString();

    switch (SchafkopfMessageType.valueOf(messageType)) {
      case HEARTBEAT_SYN:
        sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.HEARTBEAT_ACK));
        break;
      case JOIN_ONLINE_GAME:
        OnlineGameSession onlineGameSession = null;
        try {
          onlineGameSession = dedicatedServer.getGameSessionByName(
              content.get("serverName").getAsString());
        } catch (NoGameSessionException e) {
          JsonObject messageObject = new JsonObject();
          messageObject.addProperty("error",
              "No GameSession with name \"" + content.get("serverName").getAsString()
                  + "\" found.");
          sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR,
              messageObject));
          break;
        }
        joinGame(onlineGameSession);
        sendServerList();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("message",
            "Joined GameSession \"" + onlineGameSession.getServerName() + "\".");
        sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.INFO_MESSAGE,
            messageObject));
        break;
      case START_DEDICATED_GAME:
        try {
          this.onlineGameSession.startGame();
        } catch (SchafkopfException e) {
          sendError(e);
        }
        break;
      case SET_PLAYER_NAME:
        String name = content.get("playerName").getAsString();
        onlinePlayer.setName(name);
        break;
      case PLAYER_CARD:
        onlinePlayer.receiveCard(Karte.valueOf(content.get("card").getAsString()));
        break;
      case LIST_ONLINE_GAMES:
        sendServerList();
        break;
      case GET_ONLINE_GAME:
        this.onlineGameSession.sendSessionInfo();
        break;
      case CREATE_ONLINE_GAME:
        String servername = content.get("serverName").getAsString();
        OnlineGameSession onlineGameSession2 = new OnlineGameSession(servername,
            this.dedicatedServer);
        dedicatedServer.addGameSession(onlineGameSession2);
        joinGame(onlineGameSession2);
        sendServerList();
        break;
      case SET_STATUS_READY:
        onlinePlayer.setReady(!onlinePlayer.isReady());
        this.onlineGameSession.sendSessionInfo();
        break;
      case LEAVE_ONLINE_GAME:
        this.onlineGameSession.removePlayer(this.onlinePlayer);
        this.onlineGameSession = null;
        sendServerList();
        break;
      default:
        // Handle unknown message types
        logger.warn("Received unknown message type: " + messageType);
        break;
    }
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    if (this.onlineGameSession != null) {
      this.onlineGameSession.removePlayer(this.onlinePlayer);
    }
    super.onWebSocketClose(statusCode, reason);

    dedicatedServer.removeFrontendEndpoint(this);

    logger.warn("Socket Closed: [" + statusCode + "] " + reason);
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
    try {
      getRemote().sendString(schafkopfMessage.getMessageAsString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * The main entrypoint of the Application.
   */
  public void joinGame(OnlineGameSession onlineGameSession) {
    if (this.onlineGameSession != null) {
      this.onlineGameSession.removePlayer(this.onlinePlayer);
    }
    this.onlineGameSession = onlineGameSession;
    onlineGameSession.addPlayer(this.onlinePlayer);
  }

  private void sendServerList() {
    JsonObject messageObject = new JsonObject();
    try {
      messageObject.add("games", dedicatedServer.getGameSessionsAsJson());
    } catch (NoGameSessionException e) {
      JsonObject error = new JsonObject();
      error.addProperty("error",
          "No GameSessions found.");
      sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR,
          error));
      return;
    }

    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.LIST_ONLINE_GAMES,
        messageObject));
  }

  /**
   * Send a message to the connected FrontEnd.
   */
  public void sendError(SchafkopfException e) {
    JsonObject messageObject = new JsonObject();
    messageObject.addProperty("error",
        e.getMessage());
    sendMessage(new SchafkopfBaseMessage(SchafkopfMessageType.UNKNOWN_ERROR,
        messageObject));
  }
}
