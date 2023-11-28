package org.schafkopf.kartenleser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.schafkopf.BackendServer;

public class WindowsKartenLeser extends KartenLeser {

  public WindowsKartenLeser(BackendServer server) {
    super(server);

    new Thread(
            () -> {
              this.run();
            })
        .start();
  }

  public static void run() {
    // Start a server socket for Python to connect
    try (ServerSocket serverSocket = new ServerSocket(12345)) {
      System.out.println("Java server is listening on port 12345...");

      while (true) {
        try (Socket clientSocket = serverSocket.accept();
            BufferedReader in =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

          String receivedData = in.readLine();

          // Call a method or perform an action based on the received data
          handleReceivedData(receivedData);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void handleReceivedData(String data) {
    // Your method to handle the received data
    System.out.println(data.toUpperCase());
    server.nfcGelesen(data.toUpperCase());
  }
}
