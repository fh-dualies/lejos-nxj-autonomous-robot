package io.connection;

import event.EventManager;
import event.base.CommandEvent;
import io.command.ICommand;
import io.command.MoveCommand;
import io.command.SwitchStateCommand;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import state.RoboStates;

/**
 * BluetoothReceiver is responsible for managing the Bluetooth connection and receiving commands
 * from a connected device. It is intended to be used for the robot to receive commands from a remote connection.
 * It listens for incoming commands, parses them, and dispatches them to the event manager.
 */
public class BluetoothReceiver {
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
   * @param eventManager The event manager to dispatch command events to.
   * @throws NullPointerException if eventManager is null.
   */
  public BluetoothReceiver(EventManager eventManager) { this.eventManager = Objects.requireNonNull(eventManager); }

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
      System.out.println("Received command: " + commandString);

      ICommand command = this.parseCommand(commandString);

      if (command == null) {
        System.out.println("Command not recognized: " + commandString);
        return;
      }

      CommandEvent event = new CommandEvent(command);
      this.eventManager.dispatch(event);
    } catch (IOException e) {
      System.out.println("Error reading command");
      e.printStackTrace();

      LCD.clear();
      LCD.drawString("Error reading cmd", 0, 4);
      LCD.drawString("Closing connection", 0, 5);
      LCD.refresh();

      this.closeConnection();
    } catch (Exception e) {
      System.out.println("Error processing command");
      e.printStackTrace();

      this.closeConnection();
    }
  }

  /**
   * Parses a command string and returns the corresponding ICommand object.
   * It recognizes move commands (FORWARD, BACKWARD, LEFT, RIGHT, STOP) and state switch commands (IDLE, AUTONOMOUS,
   * MANUAL).
   *
   * @param commandString The command string to parse.
   * @return The corresponding ICommand object or null if the command is not recognized.
   */
  public ICommand parseCommand(String commandString) {
    if (commandString == null) {
      return null;
    }

    String command = commandString.trim().toUpperCase();

    // move commands
    switch (command) {
    case "FORWARD":
      return new MoveCommand(MoveCommand.Direction.FORWARD);
    case "BACKWARD":
      return new MoveCommand(MoveCommand.Direction.BACKWARD);
    case "LEFT":
      return new MoveCommand(MoveCommand.Direction.LEFT);
    case "RIGHT":
      return new MoveCommand(MoveCommand.Direction.RIGHT);
    case "STOP":
      return new MoveCommand(MoveCommand.Direction.STOP);
    }

    // switch state commands
    switch (command) {
    case "IDLE":
      return new SwitchStateCommand(RoboStates.IDLE);
    case "AUTONOMOUS":
      return new SwitchStateCommand(RoboStates.AUTONOMOUS);
    case "MANUAL":
      return new SwitchStateCommand(RoboStates.MANUAL);
    }

    return null;
  }

  /**
   * Establishes a Bluetooth connection and waits for a remote device to connect.
   * It displays the connection status on the LCD screen.
   *
   * @return true if the connection is established successfully, false otherwise.
   */
  public boolean waitForConnection() {
    LCD.clear();
    LCD.drawString("Waiting for BT", 0, 0);
    LCD.refresh();

    connection = Bluetooth.waitForConnection();

    if (connection == null) {
      LCD.drawString("No connection", 0, 1);
      LCD.refresh();
      this.isConnected = false;

      return false;
    }

    LCD.drawString("Connected", 0, 1);
    LCD.drawString(connection.getAddress(), 0, 2);
    LCD.refresh();

    try {
      dataStream = connection.openDataInputStream();
      this.isConnected = true;
      System.out.println("BT DataStream established");

      return true;
    } catch (Exception e) {
      System.out.println("BT DataStream not established");
      e.printStackTrace();
      LCD.drawString("Error opening stream", 0, 3);
      LCD.refresh();

      this.closeConnection();

      return false;
    }
  }

  /**
   * Closes the Bluetooth connection and the data stream.
   * It sets the isConnected flag to false and handles any exceptions that may occur during the closing process.
   */
  public void closeConnection() {
    System.out.println("Closing connection");
    this.isConnected = false;

    try {
      if (this.dataStream != null) {
        this.dataStream.close();
        this.dataStream = null;
        System.out.println("DataStream closed");
      }
    } catch (Exception e) {
      System.out.println("Error closing DataStream");
      e.printStackTrace();
    }

    try {
      if (this.connection != null) {
        this.connection.close();
        this.connection = null;
        System.out.println("Connection closed");

        LCD.clear();
        LCD.drawString("Connection closed", 0, 1);
        LCD.refresh();
      }
    } catch (Exception e) {
      System.out.println("Error closing connection");
      e.printStackTrace();
    }
  }

  /**
   * Checks if the Bluetooth connection is currently active.
   *
   * @return true if the connection is active, false otherwise.
   */
  public boolean isConnected() { return this.isConnected; }

  /**
   * Returns the active Bluetooth connection.
   *
   * @return The active BTConnection object.
   */
  public BTConnection getConnection() { return this.connection; }
}
