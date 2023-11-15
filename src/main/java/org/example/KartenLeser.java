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

       new Thread(new Runnable() {
            public void run(){
                        try {
           new KartenListener().run(); 
        } catch(Exception e){}
            }
       }).start();
    }



    public static final void main(String[] args) throws IOException {
   
    }

    private static class KartenListener implements Pn532SamThreadListener {

        @SuppressWarnings("rawtypes")
        private List<Pn532SamThread> samThreads = new ArrayList<>();

        public void run() throws IOException {
            Pn532ContextHelper.initialize();

            samThreads.add(new Pn532SamThread<>(this, new Pn532I2c()));

            for (var samThread : samThreads) {
                samThread.start();
            }

            System.out.println("Press Enter to exit...");
            System.in.read();

            for (var samThread : samThreads) {
                closeThread(samThread);
            }

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