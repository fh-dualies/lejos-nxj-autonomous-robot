package io.connection;

import java.io.DataOutputStream;
import lejos.nxt.comm.BTConnection;

public class BluetoothTransmitter {
  private BTConnection connection = null;
  private DataOutputStream dataStream = null;
  private boolean isConnected = false;

  public boolean setupConnection(BTConnection connection) {
    if (connection == null) {
      System.out.println("Connection is null");
      return false;
    }

    this.connection = connection;

    try {
      this.dataStream = connection.openDataOutputStream();
      this.isConnected = true;
      System.out.println("Bluetooth connection established");

      return true;
    } catch (Exception e) {
      System.out.println("Error opening data stream");
      e.printStackTrace();

      this.connection = null;
      this.isConnected = false;
      this.dataStream = null;

      return false;
    }
  }

  public synchronized boolean sendData(String message) {
    if (!this.isConnected || this.dataStream == null) {
      return false;
    }

    if (message == null || message.isEmpty()) {
      return false;
    }

    try {
      this.dataStream.writeUTF(message);
      this.dataStream.flush();

      return true;
    } catch (Exception e) {
      System.out.println("Error sending data");
      e.printStackTrace();

      this.isConnected = false;

      return false;
    }
  }

  public void closeStream() {
    System.out.println("Closing stream");
    this.isConnected = false;

    if (this.dataStream == null) {
      return;
    }

    try {
      this.dataStream.close();
      System.out.println("DataStream closed");
    } catch (Exception e) {
      System.out.println("Error closing DataStream");
      e.printStackTrace();
    } finally {
      this.dataStream = null;
    }
  }

  public boolean isConnected() { return this.isConnected; }
}
