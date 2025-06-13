package domain.command.impl;

import domain.command.base.ICommand;

/**
 * Command to exit the application.
 * This command is used to terminate the program or application.
 * It is part of the command pattern implementation for the robot's state management.
 */
public class ExitCommand implements ICommand {
  /**
   * Executes the exit command.
   * This method will typically be called to terminate the program or application.
   */
  public void execute() { System.exit(0); }
}
