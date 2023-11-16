package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.Pi4J;
import com.pi4j.io.i2c.I2C;
import mk.hsilomedus.pn532.*;
import mk.hsilomedus.pn532.Pn532SamThread.Pn532SamThreadListener;

public final class KartenLeser {
    
    private static BackendServer server;

    public KartenLeser(BackendServer server){
        this.server = server;

       new Thread(() -> {

      new KartenListener().run();

       }).start();
    }



    public static final void main(String[] args) throws IOException {
   
    }

    private static class KartenListener implements Pn532SamThreadListener {

        @SuppressWarnings("rawtypes")
        Pn532SamThread<I2C> i2cThread = new Pn532SamThread<>(this, new Pn532I2c());


        public void run() {
            Pn532ContextHelper.initialize();
            System.out.println("Sart Leser");
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
            System.out.println(displayName + ": UID '" + Pn532SamThreadListener.getUidString(uid) + "' received.");
            try {
                server.karteGelesen(Pn532SamThreadListener.getUidString(uid) );
            } catch (InterruptedException e) {
            }
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