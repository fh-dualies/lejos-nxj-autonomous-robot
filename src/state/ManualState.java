package state;

import core.RoboController;
import event.base.AbstractEvent;

public class ManualState extends AbstractRoboState {
  private final RoboStates state = RoboStates.MANUAL;

  public RoboStates getState() { return this.state; }

  public void onEnter(RoboController controller) {
    System.out.println("Entering manual state");

    controller.setCurrentDrivingStrategy(controller.getUserControlStrategy());
  }

  public void onExit(RoboController controller) {
    System.out.println("Exiting manual state");

    controller.getMotorController().stopMotors();
    controller.setCurrentDrivingStrategy(null);
  }

  public void handleEvent(RoboController controller, AbstractEvent event) {
    this.handleSwitchEvent(controller, event);

    // TODO: handle other events
  }
}
