package org.schafkopf.cardreader;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.tools.jconsole.JConsoleContext;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.UnsupportedEncodingException;
import org.schafkopf.BackendServer;

/** Class that represents the NFC Reader. */
public class UsbCardReader extends CardReader {

  private volatile boolean isRunning = true;
  Dotenv dotenv = Dotenv.configure().directory("./").load();
  private String comPort = null;

  /**
   * Creates an Instance of the KartenLeser.
   *
   * @param server Backend Server to call methods on.
   */
  public UsbCardReader(BackendServer server) {
    super(server);
    new Thread(this::run).start();
  }

  public void stop() {
    isRunning = false;
  }

  /** Run the reader. */
  public void run() {
    //    SerialPort[] ports = SerialPort.getCommPorts();
    //
    //    for (SerialPort port : ports) {
    //      if (port.openPort()) {
    //        System.out.println(port.getSystemPortName());
    //        try {
    //          Thread.sleep(5000);
    //        } catch (InterruptedException e) {
    //          throw new RuntimeException(e);
    //        }
    //        // Read any data available on the serial port
    //        byte[] initialBuffer = new byte[port.bytesAvailable()];
    //        int initialBytesRead = port.readBytes(initialBuffer, initialBuffer.length);
    //        String initialData = null;
    //        try {
    //          initialData = new String(initialBuffer, 0, initialBytesRead, "UTF-8").trim();
    //        } catch (UnsupportedEncodingException e) {
    //          throw new RuntimeException(e);
    //        }
    //        System.out.print("Raw data: ");
    //        for (byte b : initialBuffer) {
    //          System.out.print(b + " ");
    //        }
    //        System.out.println(initialData);
    //        if (initialData.contains("Adafruit PN532 NFC Marker")) {
    //          comPort = port.getSystemPortName();
    //        }
    //      }
    //    }

    comPort = dotenv.get("COM_PORT");

    if (comPort == null) {
      System.out.println("Adafruit PN532 NFC device not found");
      return;
    }

    SerialPort serialPort = SerialPort.getCommPort(comPort);
    serialPort.setBaudRate(115200);

    if (serialPort.openPort()) {
      System.out.println("Serial port opened successfully");

      try {
        while (isRunning) {
          if (serialPort.bytesAvailable() > 0) {
            byte[] buffer = new byte[serialPort.bytesAvailable()];
            int bytesRead = serialPort.readBytes(buffer, buffer.length);
            String data = new String(buffer, 0, bytesRead, "UTF-8").trim();

            // Process the received data
            server.nfcGelesen(data);
          }

          // Optional: Add a delay to avoid consuming too much CPU
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      } finally {
        serialPort.closePort();
        System.out.println("Serial port closed");
      }
    } else {
      System.out.println("Failed to open serial port");
    }
  }
}
