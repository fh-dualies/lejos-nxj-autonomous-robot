package io.connection.impl;

import domain.command.CommandParser;
import domain.command.base.ICommand;
import domain.event.EventManager;
import domain.event.impl.CommandEvent;
import io.connection.base.ICommunicationChannel;
import java.io.DataInputStream;
import java.io.IOException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import shared.util.LcdUtil;
import shared.util.Logger;

/**
 * BluetoothReceiver is responsible for managing the Bluetooth connection and receiving commands
 * from a connected device. It is intended to be used for the robot to receive commands from a remote connection.
 * It listens for incoming commands, parses them, and dispatches them to the event manager.
 */
public final class BluetoothReceiver implements ICommunicationChannel {
  /**
   * Used to send command events to the event manager for processing.
   */
  private final EventManager eventManager;

  /**
   * Represents the active Bluetooth connection to the remote device.
   */
  private BTConnection connection = null;

  /**
   * Used to read data from the Bluetooth connection.
   */
  private DataInputStream dataStream = null;

  /**
   * Indicates whether the Bluetooth connection is currently active.
   */
  private boolean isConnected = false;

  /**
   * Constructor that initializes the BluetoothReceiver with an EventManager instance.
   *
   * @param eventManager The event manager to dispatch command events to.
   * @throws NullPointerException if eventManager is null.
   */
  public BluetoothReceiver(EventManager eventManager) {
    if (eventManager == null) {
      throw new NullPointerException();
    }

    this.eventManager = eventManager;
  }

  /**
   * Called to check for incoming commands from the Bluetooth connection.
   * If a command is received, it is parsed and dispatched as a CommandEvent to the event manager.
   */
  public void checkForCommands() {
    if (!this.isConnected || this.dataStream == null) {
      return;
    }

    try {
      if (this.dataStream.available() <= 0) {
        return;
      }

      String commandString = this.dataStream.readUTF();
      ICommand command = CommandParser.parse(commandString);

      if (command == null) {
        Logger.warning("unknown command: " + commandString);
        return;
      }

      CommandEvent event = new CommandEvent(command);
      this.eventManager.dispatch(event);
    } catch (IOException e) {
      Logger.error("error parsing command", e);

      LcdUtil.clear();
      LcdUtil.print("Error reading cmd", LcdUtil.Position.ERROR);
      LcdUtil.print("Closing connection", LcdUtil.Position.INFO);

      this.closeConnection();
    } catch (Exception e) {
      Logger.error("error processing command", e);
      this.closeConnection();
    }
  }

  /**
   * Establishes a Bluetooth connection and waits for a remote device to connect.
   * It displays the connection status on the LCD screen.
   *
   * @return true if the connection is established successfully, false otherwise.
   */
  public boolean waitForConnection() {
    LcdUtil.clear();
    LcdUtil.print("Waiting for BT", LcdUtil.Position.INFO);

    connection = Bluetooth.waitForConnection();

    if (connection == null) {
      LcdUtil.print("No connection", LcdUtil.Position.ERROR);
      this.isConnected = false;

      return false;
    }

    LcdUtil.print("Connected", LcdUtil.Position.INFO);

    try {
      dataStream = connection.openDataInputStream();
      this.isConnected = true;

      return true;
    } catch (Exception e) {
      LcdUtil.print("Error opening stream", LcdUtil.Position.ERROR);
      this.closeConnection();

      return false;
    }
  }

  @Override
  public void closeConnection() {
    Logger.info("closing connection");
    this.isConnected = false;

    try {
      if (this.dataStream != null) {
        this.dataStream.close();
        this.dataStream = null;
      }
    } catch (Exception e) {
      Logger.error("error closing dataStream", e);
    }

    try {
      if (this.connection != null) {
        this.connection.close();
        this.connection = null;

        LcdUtil.clear();
        LcdUtil.print("Connection closed", LcdUtil.Position.INFO);
      }
    } catch (Exception e) {
      Logger.error("error closing Connection", e);
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
