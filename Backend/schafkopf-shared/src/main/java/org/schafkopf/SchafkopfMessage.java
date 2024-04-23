package org.schafkopf;

import com.google.gson.JsonObject;

/**
 * Class that represents one Frontend Connection.
 */
public class SchafkopfMessage {

  /**
   * Class that represents one Frontend Connection.
   */
  public static class SchafkopfBaseMessage {

    private JsonObject message;

    public SchafkopfBaseMessage(SchafkopfMessageType messageType, String content) {
      this.message = buildBaseMessage(messageType, content);
    }

    public SchafkopfBaseMessage(SchafkopfMessageType messageType, JsonObject content) {
      this.message = buildBaseMessage(messageType, content);
    }

    public SchafkopfBaseMessage(SchafkopfMessageType messageType) {
      this.message = buildBaseMessage(messageType);
    }

    public JsonObject getBaseMessage() {
      return message;
    }
  }

  JsonObject message;

  public SchafkopfMessage(SchafkopfMessageOrigin origin, SchafkopfBaseMessage baseMessage) {
    this.message = buildWrapperMessage(origin, baseMessage.getBaseMessage());
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public SchafkopfMessage(SchafkopfMessageOrigin origin, SchafkopfMessageType messageType) {

    JsonObject messageContentObject = new JsonObject();
    messageContentObject.add("content", buildBaseMessage(messageType));

    this.message = buildWrapperMessage(origin, messageContentObject);
  }

  /**
   * Class that represents one Frontend Connection.
   */
  private SchafkopfMessage(SchafkopfMessageOrigin origin, SchafkopfMessageType messageType,
      JsonObject messageContent) {

    JsonObject messageContentObject = new JsonObject();
    messageContentObject.add("content", buildBaseMessage(messageType, messageContent));

    this.message = buildWrapperMessage(origin, messageContentObject);
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public SchafkopfMessage(SchafkopfMessageOrigin origin, SchafkopfMessageType messageType,
      String messageContent) {

    JsonObject messageContentObject = new JsonObject();
    messageContentObject.add("content", buildBaseMessage(messageType, messageContent));

    this.message = buildWrapperMessage(origin, messageContentObject);
  }

  private static JsonObject buildWrapperMessage(SchafkopfMessageOrigin origin, JsonObject message) {
    JsonObject messageObject = new JsonObject();
    messageObject.addProperty("origin", origin.toString());
    messageObject.add("message", message);

    return messageObject;
  }

  private static JsonObject buildBaseMessage(SchafkopfMessageType messageType,
      String messageContent) {
    JsonObject messageContentObject = new JsonObject();
    messageContentObject.addProperty("message_type", messageType.toString());
    messageContentObject.addProperty("content", messageContent);

    return messageContentObject;
  }

  private static JsonObject buildBaseMessage(SchafkopfMessageType messageType,
      JsonObject messageContent) {
    JsonObject messageContentObject = new JsonObject();
    messageContentObject.addProperty("message_type", messageType.toString());
    messageContentObject.add("content", messageContent);

    return messageContentObject;
  }

  private static JsonObject buildBaseMessage(SchafkopfMessageType messageType) {
    JsonObject messageContentObject = new JsonObject();
    messageContentObject.addProperty("message_type", messageType.toString());

    return messageContentObject;
  }

  public JsonObject getMessage() {
    return message;
  }

  public String getMessageAsString() {
    return message.toString();
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public enum SchafkopfMessageType {
    UNKNOWN_ERROR,
    HEARTBEAT_SYN,
    HEARTBEAT_ACK,
    GET_CARD_ONLINE_PLAYER,
    ONLINE_PLAYER_HAND,
    GAME_STATE,
    SERVER_CONNECTION_SUCCESSFUL,
    REQUEST_SERVER_CONNECTION,
    START_GAME,
    JOIN_GAME,
    PLAYER_CARD
  }

  /**
   * Class that represents one Frontend Connection.
   */
  public enum SchafkopfMessageOrigin {
    FRONTEND,
    BACKEND,
    DEDICATED_SERVER
  }
}
