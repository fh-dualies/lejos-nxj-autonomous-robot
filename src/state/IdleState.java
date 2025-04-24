package state;

import core.RoboController;
import event.base.AbstractEvent;

public class IdleState extends AbstractRoboState {
  private final RoboStates state = RoboStates.IDLE;

  public RoboStates getState() { return this.state; }

  public void onEnter(RoboController controller) {
    System.out.println("Entering idle state");

    controller.getMotorController().stopMotors();
  }

  public void onExit(RoboController controller) { System.out.println("Exiting idle state"); }

  public void handleEvent(RoboController controller, AbstractEvent event) {
    this.handleSwitchEvent(controller, event);

    // TODO: handle other events
  }
}
