package io.command;

import state.RoboStates;

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
  private final RoboStates targetState;

  /**
   * @param targetState The state to switch to.
   * @throws NullPointerException if targetState is null.
   */
  public SwitchStateCommand(RoboStates targetState) {
    if (targetState == null) {
      throw new NullPointerException();
    }

    this.targetState = targetState;
  }

  /**
   * @return The target state to switch to.
   */
  public RoboStates getTargetState() { return this.targetState; }
}
