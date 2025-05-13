package state;

import core.RoboController;
import event.base.AbstractEvent;
import lejos.nxt.LCD;
import strategy.CalibrationStrategy;

public class CalibrationState extends AbstractRoboState {
  /**
   * The name of the calibration state.
   */
  private static final RoboStates STATE = RoboStates.CALIBRATION;

  @Override
  public RoboStates getState() {
    return STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    LCD.clear();

    controller.getContext().getMotorController().stopMotors(true);
    controller.setCurrentDrivingStrategy(new CalibrationStrategy(controller));
  }

  @Override
  public void onExit(RoboController controller) {
    LCD.clear();

    controller.setCurrentDrivingStrategy(null);
  }

  /**
   * Handles events that occur while in the calibration state.
   * This method processes the event and calls the appropriate methods to handle it.
   * It also handles state switching events by calling the handleSwitchEvent method.
   * Will be called by the RoboController after an event is received.
   *
   * @param controller The RoboController instance that manages the robot's state.
   * @param event      The event that occurred.
   */
  public void handleEvent(RoboController controller, AbstractEvent event) { this.handleSwitchEvent(controller, event); }
}
