import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

public class RobotoApplication {
  public static void main(String[] args) {
    LCD.clear();
    LCD.drawString("Waiting for BT", 0, 0);
    NXTConnection conn = Bluetooth.waitForConnection();

    LCD.clear();
    LCD.drawString("Connected", 0, 0);

    DataOutputStream dos = conn.openDataOutputStream();
    DataInputStream dis = conn.openDataInputStream();

    try {
      while (true) {
        if (Button.LEFT.isDown()) {
          LCD.clear();
          LCD.drawString("Stopped by user", 0, 0);
          break;
        }

        if (dis.available() > 0) {
          String receivedData = dis.readLine();

          LCD.clear();
          LCD.drawString("Received:", 0, 0);
          LCD.drawString(receivedData, 0, 1);

          dos.writeBytes(receivedData);
          dos.flush();

          LCD.drawString("Sent back:", 0, 3);
          LCD.drawString(receivedData, 0, 4);
        } else {
          LCD.drawString("No data", 0, 1);
        }

        Thread.sleep(50);
      }
    } catch (IOException ioe) {
      LCD.clear();
      LCD.drawString("IO Exception", 0, 0);
      LCD.drawString(ioe.getMessage(), 0, 1);
    } catch (InterruptedException ie) {
    } finally {
      try {
        dos.close();
        dis.close();
        conn.close();
      } catch (IOException ioe) {
        LCD.clear();
        LCD.drawString("Close Exception", 0, 0);
        LCD.drawString(ioe.getMessage(), 0, 1);
      }
    }
  }
}
