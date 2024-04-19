package org.schafkopf;

import java.util.Timer;
import java.util.TimerTask;

/** Creates an Instance of the Backend Server. */
public class HeartbeatSender {
  private static final int HEARTBEAT_INTERVAL = 15000; // 1 minute

  private final DedicatedServerConnection client;

  public HeartbeatSender(DedicatedServerConnection client) {
    this.client = client;
  }

  /** Creates an Instance of the Backend Server. */
  public void start() {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        client.sendMessage("HEARTBEAT SYN"); // Send a heartbeat message
      }
    }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);
  }
}
