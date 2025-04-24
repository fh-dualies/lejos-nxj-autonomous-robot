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

public class BluetoothReceiver {
  private final EventManager eventManager;
  private BTConnection connection = null;
  private DataInputStream dataStream = null;
  private boolean isConnected = false;

  public BluetoothReceiver(EventManager eventManager) { this.eventManager = Objects.requireNonNull(eventManager); }

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

  public boolean isConnected() { return this.isConnected; }
}
