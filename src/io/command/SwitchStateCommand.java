package io.command;

import io.abstracts.ICommand;
import io.constants.RoboStateEnum;

/**
 * Command to switch the state of the robot.
 * This command is used to change the current state of the robot to a new state.
 * It is part of the command pattern implementation for the robot's state management.
 */
public class SwitchStateCommand implements ICommand {
  /**
   * The target state to switch to.
   * This field holds the new state that the robot should transition to.
   */
  private final RoboStateEnum targetState;

  /**
   * Constructor for the SwitchStateCommand class.
   *
   * @param targetState The state to switch to.
   * @throws NullPointerException if targetState is null.
   */
  public SwitchStateCommand(RoboStateEnum targetState) {
    if (targetState == null) {
      throw new NullPointerException();
    }

    this.targetState = targetState;
  }

  /**
   * Executes the command to switch the robot's state.
   *
   * @return The target state to switch to.
   */
  public RoboStateEnum getTargetState() { return this.targetState; }
}
