package io.connection;

import lejos.nxt.comm.BTConnection;

/**
 * ICommunicationChannel is an interface that defines the methods for managing a communication channel.
 * It provides methods to close the connection, check if the connection is active, and get the current connection.
 */
public interface ICommunicationChannel {
  /**
   * Closes the current connection and releases any resources associated with it.
   */
  void closeConnection();

  /**
   * Checks if the current connection is active.
   *
   * @return true if the connection is active, false otherwise.
   */
  boolean isConnected();

  /**
   * Returns the active Bluetooth connection.
   *
   * @return The active BTConnection object.
   */
  BTConnection getConnection();
}
