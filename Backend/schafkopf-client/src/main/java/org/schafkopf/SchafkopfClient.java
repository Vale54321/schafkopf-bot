package org.schafkopf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageOrigin;
import org.schafkopf.SchafkopfMessage.SchafkopfMessageType;

/**
 * Class that represents one Frontend Connection.
 */
public class SchafkopfClient implements MessageListener {

  private BackendServer backendServer;
  private DedicatedServerConnection dedicatedServerConnection;

  /**
   * Class that represents one Frontend Connection.
   */
  public SchafkopfClient() throws Exception {

    this.backendServer = new BackendServer("localhost", 8080, true, this);

    System.out.println("Client started.");
  }

  public static void main(String[] args) throws Exception {
    new SchafkopfClient();
  }

  @Override
  public void receiveMessage(String jsonMessage) {
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(jsonMessage, JsonObject.class);

    // Check if the origin is "frontend" or "dedicated_server"
    String origin = jsonObject.get("origin").getAsString();
    switch (SchafkopfMessageOrigin.valueOf(origin)) {
      case FRONTEND:
        handleFrontendMessage(jsonObject);
        break;
      case DEDICATED_SERVER:
        handleDedicatedServerMessage(jsonObject);
        break;
      default:
        // Handle messages from unknown origins
        System.out.println("Received message from unknown origin: " + origin);
        break;
    }
  }


  private void handleFrontendMessage(JsonObject jsonObject) {
    JsonObject message = jsonObject.getAsJsonObject("message");
    JsonObject content = message.getAsJsonObject("content");
    String messageType = message.get("message_type").getAsString();

    switch (SchafkopfMessageType.valueOf(messageType)) {
      case REQUEST_SERVER_CONNECTION:
        dedicatedServerConnection = new DedicatedServerConnection(
            content.get("serverAddress").getAsString(),
            this);
        break;
      case PLAYER_CARD:
      case CREATE_ONLINE_GAME:
      case JOIN_ONLINE_GAME:
      case SET_PLAYER_NAME:
        dedicatedServerConnection.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessageType.valueOf(messageType), content));
        break;
      case LIST_ONLINE_GAMES:
      case GET_ONLINE_GAME:
      case SET_STATUS_READY:
      case LEAVE_ONLINE_GAME:
      case START_DEDICATED_GAME:
        dedicatedServerConnection.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessageType.valueOf(messageType)));
        break;
      default:
        // Handle unknown message types
        System.out.println("Received unknown message type from frontend server: " + messageType);
        break;
    }

    System.out.println("Received message from frontend: " + jsonObject);
  }

  private void handleDedicatedServerMessage(JsonObject jsonObject) {
    JsonObject message = jsonObject.getAsJsonObject("message");
    JsonObject content = message.getAsJsonObject("content");
    String messageType = message.get("message_type").getAsString();

    switch (SchafkopfMessageType.valueOf(messageType)) {
      case GET_CARD_ONLINE_PLAYER:
      case HEARTBEAT_ACK:

        break;
      case GAME_STATE:
      case ONLINE_PLAYER_HAND:
      case UNKNOWN_ERROR:
      case INFO_MESSAGE:
      case GET_ONLINE_GAME:
      case LIST_ONLINE_GAMES:
        backendServer.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessageType.valueOf(messageType), content));
        break;
      case SERVER_CONNECTION_SUCCESSFUL:
      case GAME_START_READY:
        backendServer.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessageType.valueOf(messageType)));
        break;
      default:
        // Handle unknown message types
        System.out.println("Received unknown message type from dedicated server: " + messageType);
        break;
    }
    if (!messageType.equals("HEARTBEAT_ACK")) {
      System.out.println("Received message from dedicated server: " + jsonObject);
    }
  }
}

