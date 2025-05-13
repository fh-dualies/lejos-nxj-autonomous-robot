package io.connection;

import event.CommandEvent;
import event.EventManager;
import io.command.ExitCommand;
import io.command.ICommand;
import io.command.MoveCommand;
import io.command.SwitchStateCommand;
import java.io.DataInputStream;
import java.io.IOException;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import state.RoboStates;
import util.Log;
import util.StringUtil;

/**
 * BluetoothReceiver is responsible for managing the Bluetooth connection and receiving commands
 * from a connected device. It is intended to be used for the robot to receive commands from a remote connection.
 * It listens for incoming commands, parses them, and dispatches them to the event manager.
 */
public class BluetoothReceiver implements ICommunicationChannel {
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
      ICommand command = this.parseCommand(commandString);

      if (command == null) {
        Log.warning("unknown command: " + commandString);
        return;
      }

      CommandEvent event = new CommandEvent(command);
      this.eventManager.dispatch(event);
    } catch (IOException e) {
      Log.error("error parsing command", e);

      LCD.clear();
      LCD.drawString("Error reading cmd", 0, 4);
      LCD.drawString("Closing connection", 0, 5);
      LCD.refresh();

      this.closeConnection();
    } catch (Exception e) {
      Log.error("error processing command", e);
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
    if (commandString == null || commandString.isEmpty()) {
      return null;
    }

    String command = commandString.trim().toUpperCase();

    if (command.equals("EXIT")) {
      return new ExitCommand();
    }

    // switch state commands
    ICommand result;
    result = this.parseStateCommand(command);
    if (result != null) {
      return result;
    }

    // move commands
    result = this.parseMoveCommand(command);
    if (result != null) {
      return result;
    }

    Log.warning("unknown command: " + command);

    return null;
  }

  /**
   * Parses a move command string and returns the corresponding MoveCommand object.
   * The command should be in the format "MOVE(speed, turnAngle)".
   *
   * @param command The command string to parse.
   * @return The corresponding MoveCommand object or null if the command is not recognized.
   */
  private ICommand parseMoveCommand(String command) {
    if (command == null || command.isEmpty()) {
      return null;
    }

    String[] parts = StringUtil.split(command, "|");

    if (parts.length != 3) {
      return null;
    }

    String cmd = parts[0].trim();
    if (!cmd.equals("MOVE")) {
      return null;
    }

    try {
      String speedString = parts[1].trim();
      String turnAngleString = parts[2].trim();

      int speed = Integer.parseInt(speedString);
      int turnAngle = Integer.parseInt(turnAngleString);

      return new MoveCommand(speed, turnAngle);
    } catch (NumberFormatException e) {
      Log.warning("invalid command value: " + command);
      return null;
    } catch (IllegalArgumentException e) {
      Log.warning("invalid command param: " + command);
      return null;
    }
  }

  private ICommand parseStateCommand(String command) {
    if (command == null || command.isEmpty()) {
      return null;
    }

    String[] parts = StringUtil.split(command, "|");

    if (parts.length != 2) {
      return null;
    }

    String cmd = parts[0].trim();
    if (!cmd.equals("STATE")) {
      return null;
    }

    String stateString = parts[1].trim();

    switch (stateString) {
    case "IDLE":
      return new SwitchStateCommand(RoboStates.IDLE);
    case "AUTONOMOUS":
      return new SwitchStateCommand(RoboStates.AUTONOMOUS);
    case "MANUAL":
      return new SwitchStateCommand(RoboStates.MANUAL);
    }

    Log.warning("unknown state in command: " + command);
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

      return true;
    } catch (Exception e) {
      LCD.drawString("Error opening stream", 0, 3);
      LCD.refresh();

      this.closeConnection();

      return false;
    }
  }

  @Override
  public void closeConnection() {
    Log.info("closing connection");
    this.isConnected = false;

    try {
      if (this.dataStream != null) {
        this.dataStream.close();
        this.dataStream = null;
      }
    } catch (Exception e) {
      Log.error("error closing dataStream", e);
    }

    try {
      if (this.connection != null) {
        this.connection.close();
        this.connection = null;

        LCD.clear();
        LCD.drawString("Connection closed", 0, 1);
        LCD.refresh();
      }
    } catch (Exception e) {
      Log.error("error closing Connection", e);
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
