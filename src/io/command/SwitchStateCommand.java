package io.command;

import state.RoboStates;

public class SwitchStateCommand implements ICommand {
  private final RoboStates targetState;
  private final RoboStates currentState;

  public SwitchStateCommand(RoboStates targetState, RoboStates currentState) {
    this.targetState = targetState;
    this.currentState = currentState;
  }

  @Override
  public void execute() {
    // TODO: implement
    System.out.println("Switching from " + currentState.getClass().getSimpleName() + " to " +
                       targetState.getClass().getSimpleName());
  }

  public RoboStates getTargetState() { return this.targetState; }

  public RoboStates getCurrentState() { return this.currentState; }
}
