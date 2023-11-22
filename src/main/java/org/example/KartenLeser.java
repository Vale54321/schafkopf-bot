package org.example;

import com.pi4j.io.i2c.I2C;
import java.io.IOException;
import mk.hsilomedus.pn532.Pn532ContextHelper;
import mk.hsilomedus.pn532.Pn532I2c;
import mk.hsilomedus.pn532.Pn532SamThread;
import mk.hsilomedus.pn532.Pn532SamThread.Pn532SamThreadListener;

public final class KartenLeser {

  private static BackendServer server;

  public KartenLeser(BackendServer server) {
    this.server = server;

    new Thread(
            () -> {
              new KartenListener().run();
            })
        .start();
  }

  public static final void main(String[] args) throws IOException {}

  private static class KartenListener implements Pn532SamThreadListener {

    @SuppressWarnings("rawtypes")
    Pn532SamThread<I2C> i2cThread = new Pn532SamThread<>(this, new Pn532I2c());

    public void run() {
      Pn532ContextHelper.initialize();
      i2cThread.start();
    }

    public void close() {
      closeThread(i2cThread);
      Pn532ContextHelper.shutdown();
    }

    @Override
    public void receiveMessage(String message) {
      System.out.println(message);
    }

    @Override
    public void uidReceived(String displayName, byte[] uid) {
      server.nfcGelesen(Pn532SamThreadListener.getUidString(uid));
    }

    @SuppressWarnings("rawtypes")
    private void closeThread(Pn532SamThread thread) {
      if (thread != null && thread.isAlive()) {
        thread.close();

        try {
          thread.join();
        } catch (InterruptedException e) {
          System.out.println("Error closing thread: " + e.getMessage());
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
