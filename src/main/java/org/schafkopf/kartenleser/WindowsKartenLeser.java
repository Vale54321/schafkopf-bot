package org.schafkopf.kartenleser;

import com.fazecast.jSerialComm.SerialPort;
import java.io.UnsupportedEncodingException;
import org.schafkopf.BackendServer;

public class WindowsKartenLeser extends KartenLeser {

  private volatile boolean isRunning = true;

  public WindowsKartenLeser(BackendServer server) {
    super(server);

    new Thread(this::run).start();
  }

  public void stop() {
    isRunning = false;
  }

  public void run() {
    SerialPort[] ports = SerialPort.getCommPorts();

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
            System.out.println(data);
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
