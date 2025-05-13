package event;

import event.base.AbstractEvent;
import event.base.IExposableEvent;
import state.RoboStates;

/**
 * ChangeStateEvent is an event that represents a change in the state of the robot.
 * It extends the AbstractEvent class and contains a reference to the new state of the robot.
 */
public class ChangeStateEvent extends AbstractEvent implements IExposableEvent {
  /**
   * The new state of the robot that generated the event.
   */
  private final RoboStates newState;

  /**
   * @param newState the new state of the robot that generated the event
   */
  public ChangeStateEvent(RoboStates newState) {
    if (newState == null) {
      throw new NullPointerException();
    }

    this.newState = newState;
  }

  /**
   * @return the new state of the robot that generated the event
   */
  public RoboStates getNewState() { return this.newState; }

  @Override
  public String toExposableString() {
    return "NEW_STATE|" + this.newState;
  }
}
