package org.schafkopf.kartenleser;

import org.schafkopf.BackendServer;

/** Class that represents one Card Reader. */
public abstract class KartenLeser {

  protected static BackendServer server;

  public KartenLeser(BackendServer server) {
    this.server = server;
  }
}
