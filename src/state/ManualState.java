package state;

import core.RoboController;
import event.base.AbstractEvent;
import lejos.nxt.LCD;
import strategy.UserControlStrategy;
import util.Log;

public class ManualState extends AbstractRoboState {
  /**
   * The name of the idle state.
   */
  private static final RoboStates STATE = RoboStates.MANUAL;

  @Override
  public RoboStates getState() {
    return STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Log.info("enter manual");
    LCD.clear();
    LCD.drawString("Manual", 0, 1);

    controller.setCurrentDrivingStrategy(new UserControlStrategy(controller));
  }

  @Override
  public void onExit(RoboController controller) {
    Log.info("exit manual");

    controller.getContext().getMotorController().stopMotors(false);
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
  public void handleEvent(RoboController controller, AbstractEvent event) { this.handleSwitchEvent(controller, event); }
}
