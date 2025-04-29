package io.connection;

import java.io.DataOutputStream;
import lejos.nxt.comm.BTConnection;
import util.Log;

/**
 * BluetoothTransmitter is a class that handles the Bluetooth connection and data transmission.
 * It is intended to be as the remote control for the robot.
 * It provides methods to set up the connection, send data, and close the stream.
 */
public class BluetoothTransmitter implements ICommunicationChannel {
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
      Log.warning("connection is null");
      return false;
    }

    this.connection = connection;

    try {
      this.dataStream = connection.openDataOutputStream();
      this.isConnected = true;

      Log.info("bt connected");

      return true;
    } catch (Exception e) {
      Log.error("error opening data stream", e);

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
  public synchronized boolean sendMessage(String message) {
    if (!this.isConnected || this.dataStream == null) {
      return false;
    }

    if (message == null || message.isEmpty()) {
      Log.warning("empty message");
      return false;
    }

    try {
      this.dataStream.writeUTF(message);
      this.dataStream.flush();

      return true;
    } catch (Exception e) {
      Log.error("error sending", e);
      this.isConnected = false;

      return false;
    }
  }

  @Override
  public void closeConnection() {
    this.isConnected = false;

    if (this.dataStream == null) {
      return;
    }

    try {
      this.dataStream.close();
      Log.info("connection closed");
    } catch (Exception e) {
      Log.error("error closing dataStream", e);
    } finally {
      this.dataStream = null;
    }
  }

  @Override
  public boolean isConnected() {
    return this.isConnected;
  }

  @Override
  public BTConnection getConnection() {
    return this.connection;
  }
}
