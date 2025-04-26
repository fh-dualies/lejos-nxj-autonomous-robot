package io.connection;

import java.io.DataOutputStream;
import lejos.nxt.comm.BTConnection;

/**
 * BluetoothTransmitter is a class that handles the Bluetooth connection and data transmission.
 * It is intended to be as the remote control for the robot.
 * It provides methods to set up the connection, send data, and close the stream.
 */
public class BluetoothTransmitter {
  /*
   * The BluetoothTransmitter class is responsible for managing the Bluetooth connection
   * and sending data to the connected device.
   */
  private BTConnection connection = null;

  /**
   * Used to send data to the Bluetooth connection.
   */
  private DataOutputStream dataStream = null;

  /**
   * Indicates whether the Bluetooth connection is currently active.
   */
  private boolean isConnected = false;

  /**
   * Sets up the Bluetooth connection and initializes the data stream.
   *
   * @param connection The Bluetooth connection to set up.
   * @return true if the connection is successfully established, false otherwise.
   */
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

  /**
   * Sends data to the connected Bluetooth device.
   *
   * @param message The message to send.
   * @return true if the data is successfully sent, false otherwise.
   */
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

  /**
   * Closes the Bluetooth data stream.
   * It sets the isConnected flag to false and handles any exceptions that may occur during the closing process.
   */
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

  /**
   * @return boolean indicating if the connection is active
   */
  public boolean isConnected() { return this.isConnected; }
}
