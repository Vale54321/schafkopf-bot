package org.schafkopf;

/**
 * Class that represents one Frontend Connection.
 */
public interface MessageListener {

  void receiveMessage(String message);
}
