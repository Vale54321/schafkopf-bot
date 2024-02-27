package org.schafkopf.cardreader;

import org.schafkopf.BackendServer;

/** Class that represents one Card Reader. */
public abstract class CardReader {

  protected static BackendServer server;

  public CardReader(BackendServer server) {
    this.server = server;
  }
}
