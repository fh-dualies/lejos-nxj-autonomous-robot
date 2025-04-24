package state;

import core.RoboController;
import event.base.AbstractEvent;

public class AutonomousState extends AbstractRoboState {
  private final RoboStates state = RoboStates.AUTONOMOUS;

  public RoboStates getState() { return this.state; }

  public void onEnter(RoboController controller) {
    System.out.println("Entering autonomous state");

    controller.setCurrentDrivingStrategy(controller.getLineFollowingStrategy());
  }

  public void onExit(RoboController controller) {
    System.out.println("Exiting autonomous state");

    controller.getMotorController().stopMotors();
    controller.setCurrentDrivingStrategy(null);
  }

  public void handleEvent(RoboController controller, AbstractEvent event) {
    this.handleSwitchEvent(controller, event);

    // TODO: handle other events
  }
}
