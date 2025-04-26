package state;

import core.RoboController;
import event.base.AbstractEvent;
import util.Log;

/*
 * IdleState is a concrete implementation of the AbstractRoboState class.
 * This class represents the idle state of the robot, where it stops all motors and waits for commands.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class IdleState extends AbstractRoboState {
  /**
   * The name of the idle state.
   */
  private final RoboStates STATE = RoboStates.IDLE;

  @Override
  public RoboStates getState() {
    return this.STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Log.info("Entering idle state");

    controller.getMotorController().stopMotors(false);
  }

  @Override
  public void onExit(RoboController controller) {
    Log.info("Exiting idle state");
  }

  /**
   * Handles events that occur while in the idle state.
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
