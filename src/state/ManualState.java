package state;

import core.RoboController;
import event.base.AbstractEvent;
import lejos.nxt.LCD;
import util.Log;

public class ManualState extends AbstractRoboState {
  private final RoboStates STATE = RoboStates.MANUAL;

  @Override
  public RoboStates getState() {
    return this.STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Log.info("Entering manual state");
    LCD.clear();
    LCD.drawString("Manual", 0, 1);

    controller.setCurrentDrivingStrategy(controller.getUserControlStrategy());
  }

  @Override
  public void onExit(RoboController controller) {
    Log.info("Exiting manual state");

    controller.getMotorController().stopMotors(false);
    controller.setCurrentDrivingStrategy(null);
  }

  /**
   * Handles events that occur while in the manual state.
   * This method processes the event and calls the appropriate methods to handle it.
   * It also handles state switching events by calling the handleSwitchEvent method.
   * Will be called by the RoboController after an event is received.
   *
   * @param controller The RoboController instance that manages the robot's state.
   * @param event      The event that occurred.
   */
  public void handleEvent(RoboController controller, AbstractEvent event) {
    this.handleSwitchEvent(controller, event);

    // TODO: handle other events
  }
}
