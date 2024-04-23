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
    // Check if the origin is "frontend"
    String origin = jsonObject.get("origin").getAsString();
    if (SchafkopfMessageOrigin.FRONTEND.toString().equals(origin)) {
      JsonObject message = jsonObject.getAsJsonObject("message");
      JsonObject content = message.getAsJsonObject("content");
      String messageType = message.get("message_type").getAsString();
      if (SchafkopfMessageType.REQUEST_SERVER_CONNECTION.toString().equals(messageType)) {
        dedicatedServerConnection = new DedicatedServerConnection(
            content.get("serverAddress").getAsString(),
            this);
      } else if ("JOIN_GAME".equals(messageType)) {
        dedicatedServerConnection.join();
      } else if ("START_DEDICATED_GAME".equals(messageType)) {
        dedicatedServerConnection.start();
      } else if (SchafkopfMessageType.PLAYER_CARD.toString().equals(messageType)) {
        dedicatedServerConnection.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessageType.PLAYER_CARD, content));
      }

      System.out.println("Received message from frontend server: " + jsonMessage);

    } else if (SchafkopfMessageOrigin.DEDICATED_SERVER.toString().equals(origin)) {

      JsonObject message = jsonObject.getAsJsonObject("message");
      JsonObject content = message.getAsJsonObject("content");
      String messageType = message.get("message_type").getAsString();
      if (SchafkopfMessageType.GET_CARD_ONLINE_PLAYER.toString().equals(messageType)) {
        System.out.println("Received get_card_online_player message from dedicated server.");
      } else if (SchafkopfMessageType.GAME_STATE.toString().equals(messageType)) {
        backendServer.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessage.SchafkopfMessageType.GAME_STATE, content));
      } else if (SchafkopfMessageType.ONLINE_PLAYER_HAND.toString().equals(messageType)) {
        backendServer.sendMessage(
            new SchafkopfBaseMessage(SchafkopfMessage.SchafkopfMessageType.ONLINE_PLAYER_HAND,
                content));
      } else if (SchafkopfMessageType.SERVER_CONNECTION_SUCCESSFUL.toString().equals(messageType)) {
        System.out.println("Received server_connection_successful message from dedicated server.");
        backendServer.sendMessage(new SchafkopfBaseMessage(
            SchafkopfMessage.SchafkopfMessageType.SERVER_CONNECTION_SUCCESSFUL));
      } else if (SchafkopfMessageType.HEARTBEAT_ACK.toString().equals(messageType)) {
        return;
      }
      System.out.println("Received message from dedicated server: " + jsonMessage);
    }
  }
}

