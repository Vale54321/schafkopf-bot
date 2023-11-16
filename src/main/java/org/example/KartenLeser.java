package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mk.hsilomedus.pn532.*;
import mk.hsilomedus.pn532.Pn532SamThread.Pn532SamThreadListener;

public final class KartenLeser {
    
    private static BackendServer server;

    public KartenLeser(BackendServer server){
        this.server = server;

       new Thread(() -> {
                   try {
      new KartenListener().run();
   } catch(Exception ignored){}
       }).start();
    }



    public static final void main(String[] args) throws IOException {
   
    }

    private static class KartenListener implements Pn532SamThreadListener {

        @SuppressWarnings("rawtypes")
        private Pn532SamThread samThread = new Pn532SamThread<>(this, new Pn532I2c());

        public void run() throws IOException {
            Pn532ContextHelper.initialize();
            samThread.start();
        }

        public void close() {
            closeThread(samThread);
            Pn532ContextHelper.shutdown();
        }

        @Override
        public void receiveMessage(String message) {
            //System.out.println(message);
        }

        @Override
        public void uidReceived(String displayName, byte[] uid) {
            System.out.println(displayName + ": UID '" + Pn532SamThreadListener.getUidString(uid) + "' received.");
            server.karteGelesen(Pn532SamThreadListener.getUidString(uid) );
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