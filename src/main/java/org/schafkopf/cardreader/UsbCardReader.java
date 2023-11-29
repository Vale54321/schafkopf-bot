package org.schafkopf.cardreader;

import com.fazecast.jSerialComm.SerialPort;
import java.io.UnsupportedEncodingException;
import org.schafkopf.BackendServer;

/** Class that represents the NFC Reader. */
public class UsbCardReader extends CardReader {

  private volatile boolean isRunning = true;

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

  /** run the reader. */
  public void run() {
    SerialPort[] ports = SerialPort.getCommPorts();
    SerialPort selectedPort = null;

    for (SerialPort port : ports) {
      if (port.getSystemPortName().equals("COM6")) {
        selectedPort = port;
        break;
      }
    }

    if (selectedPort == null) {
      System.out.println("COM6 not found");
      return;
    }

    if (ports.length == 0) {
      System.out.println("No serial ports found");
      return;
    }

    SerialPort serialPort = ports[0]; // You may need to adjust this based on your setup
    serialPort.setBaudRate(115200);

    if (serialPort.openPort()) {
      System.out.println("Serial port opened successfully");

      try {
        while (isRunning) {
          if (serialPort.bytesAvailable() > 0) {
            byte[] buffer = new byte[serialPort.bytesAvailable()];
            int bytesRead = serialPort.readBytes(buffer, buffer.length);

            String data = new String(buffer, 0, bytesRead, "UTF-8").trim();
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
