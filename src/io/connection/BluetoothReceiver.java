package io.connection;

import event.CommandEvent;
import event.EventManager;
import io.command.*;
import java.io.DataInputStream;
import java.io.IOException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import state.RoboStates;
import strategy.CalibrationStepEnum;
import util.LcdUtil;
import util.Log;
import util.StringUtil;

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
      ICommand command = this.parseCommand(commandString);

      if (command == null) {
        Log.warning("unknown command: " + commandString);
        return;
      }

      CommandEvent event = new CommandEvent(command);
      this.eventManager.dispatch(event);
    } catch (IOException e) {
      Log.error("error parsing command", e);

      LcdUtil.clear();
      LcdUtil.print("Error reading cmd", LcdUtil.Position.ERROR);
      LcdUtil.print("Closing connection", LcdUtil.Position.INFO);

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

    // calibration command
    result = this.parseCalibrationCommand(command);
    if (result != null) {
      return result;
    }

    Log.warning("unknown command: " + command);

    return null;
  }

  /**
   * Parses a calibration command string and returns the corresponding CalibrationCommand object.
   * The command should be in the format "CALIBRATE(step)".
   *
   * @param command The command string to parse.
   * @return The corresponding CalibrationCommand object or null if the command is not recognized.
   */
  private ICommand parseCalibrationCommand(String command) {
    if (command == null || command.isEmpty()) {
      return null;
    }

    String[] parts = StringUtil.split(command, "|");

    if (parts.length != 2 || !parts[0].trim().equals("CALIBRATE")) {
      return null;
    }

    String stepString = parts[1].trim();

    try {
      return new CalibrationCommand(CalibrationStepEnum.valueOf(stepString));
    } catch (IllegalArgumentException e) {
      Log.warning("unknown calibration step in command: " + command);
      return null;
    }
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
    default:
      Log.warning("unknown state in command: " + command);
      return null;
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

        LcdUtil.clear();
        LcdUtil.print("Connection closed", LcdUtil.Position.INFO);
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
