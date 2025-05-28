package io.command;

import io.abstracts.ICommand;
import io.constants.CalibrationStepEnum;
import io.constants.OrientationEnum;
import io.constants.RoboStateEnum;
import util.Logger;
import util.StringUtil;

/**
 * CommandParser is responsible for parsing command strings into ICommand objects.
 * It recognizes various commands such as move commands, state switch commands, and calibration commands.
 * The command strings should follow a specific format to be correctly parsed.
 */
public class CommandParser {
  /**
   * Parses a command string and returns the corresponding ICommand object.
   * It recognizes move commands (FORWARD, BACKWARD, LEFT, RIGHT, STOP) and state switch commands (IDLE, AUTONOMOUS,
   * MANUAL).
   *
   * @param commandString The command string to parse.
   * @return The corresponding ICommand object or null if the command is not recognized.
   */
  public static ICommand parse(String commandString) {
    if (commandString == null || commandString.isEmpty()) {
      return null;
    }

    String command = commandString.trim().toUpperCase();

    if (command.equals("EXIT")) {
      return new ExitCommand();
    }

    // switch state commands
    ICommand result;
    result = parseStateCommand(command);
    if (result != null) {
      return result;
    }

    // move commands
    result = parseMoveCommand(command);
    if (result != null) {
      return result;
    }

    // calibration command
    result = parseCalibrationCommand(command);
    if (result != null) {
      return result;
    }

    result = parseOrientationCommand(command);
    if (result != null) {
      return result;
    }

    Logger.warning("unknown command: " + command);

    return null;
  }

  private static ICommand parseOrientationCommand(String command) {
    if (command == null || command.isEmpty()) {
      return null;
    }

    String[] parts = StringUtil.split(command, "|");
    if (parts.length != 2 || !parts[0].trim().equals("ORIENT")) {
      return null;
    }

    String orientationString = parts[1].trim();
    try {
      return new OrientationCommand(OrientationEnum.valueOf(orientationString));
    } catch (IllegalArgumentException e) {
      Logger.warning("unknown orientation in command: " + command);
      return null;
    }
  }

  /**
   * Parses a calibration command string and returns the corresponding CalibrationCommand object.
   * The command should be in the format "CALIBRATE(step)".
   *
   * @param command The command string to parse.
   * @return The corresponding CalibrationCommand object or null if the command is not recognized.
   */
  private static ICommand parseCalibrationCommand(String command) {
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
      Logger.warning("unknown calibration step in command: " + command);
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
  private static ICommand parseMoveCommand(String command) {
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
      Logger.warning("invalid command value: " + command);
      return null;
    } catch (IllegalArgumentException e) {
      Logger.warning("invalid command param: " + command);
      return null;
    }
  }

  /**
   * Parses an orientation command string and returns the corresponding OrientationCommand object.
   * The command should be in the format "ORIENT(angle)".
   *
   * @param command The command string to parse.
   * @return The corresponding OrientationCommand object or null if the command is not recognized.
   */
  private static ICommand parseStateCommand(String command) {
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
      return new SwitchStateCommand(RoboStateEnum.IDLE);
    case "AUTONOMOUS":
      return new SwitchStateCommand(RoboStateEnum.AUTONOMOUS);
    case "MANUAL":
      return new SwitchStateCommand(RoboStateEnum.MANUAL);
    default:
      Logger.warning("unknown state in command: " + command);
      return null;
    }
  }
}
