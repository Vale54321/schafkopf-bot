package org.schafkopf.kartenleser;

import org.schafkopf.BackendServer;

public abstract class KartenLeser {

  protected static BackendServer server;

  public KartenLeser(BackendServer server) {
    this.server = server;
  }
}
