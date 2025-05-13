package event;

import event.base.AbstractEvent;
import event.base.IExposableEvent;
import state.RoboStates;

public class ChangeStateEvent extends AbstractEvent implements IExposableEvent {
  private final RoboStates newState;

  public ChangeStateEvent(RoboStates newState) {
    if (newState == null) {
      throw new NullPointerException();
    }

    this.newState = newState;
  }

  public RoboStates getNewState() { return this.newState; }

  @Override
  public String toExposableString() {
    return "NEW_STATE|" + this.newState;
  }
}
