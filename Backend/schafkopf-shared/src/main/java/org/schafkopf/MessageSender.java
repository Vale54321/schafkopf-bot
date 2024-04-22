package org.schafkopf;

import org.schafkopf.SchafkopfMessage.SchafkopfBaseMessage;

/**
 * The main entrypoint of the Application.
 */
public interface MessageSender {

  void sendMessage(SchafkopfBaseMessage message);
}
