package state;

import core.RoboController;
import event.base.AbstractEvent;
import lejos.nxt.LCD;
import strategy.LineFollowingStrategy;
import strategy.algorithm.ZigZagAlgorithm;
import util.Log;

/**
 * AutonomousState is a concrete implementation of the AbstractRoboState class.
 * This class represents the autonomous state of the robot, where it follows a line using its sensors.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class AutonomousState extends AbstractRoboState {
  /**
   * The name of the autonomous state.
   */
  private static final RoboStates STATE = RoboStates.AUTONOMOUS;

  @Override
  public RoboStates getState() {
    return STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Log.info("Entering autonomous state");
    LCD.clear();
    LCD.drawString("Autonomous", 0, 1);

    controller.setCurrentDrivingStrategy(new LineFollowingStrategy(new ZigZagAlgorithm(controller)));
  }

  @Override
  public void onExit(RoboController controller) {
    Log.info("Exiting autonomous state");

    controller.getContext().getMotorController().stopMotors(false);
    controller.setCurrentDrivingStrategy(null);
  }

  /**
   * Handles events that occur while in the autonomous state.
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
