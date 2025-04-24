package io.command;

import state.RoboStates;

public class SwitchStateCommand implements ICommand {
  private final RoboStates targetState;

  public SwitchStateCommand(RoboStates targetState) { this.targetState = targetState; }

  public RoboStates getTargetState() { return this.targetState; }
}
