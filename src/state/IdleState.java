package state;

import core.RoboController;
import event.abstracts.AbstractEvent;
import io.constants.RoboStateEnum;
import state.abstracts.AbstractRoboState;
import util.LcdUtil;
import util.Logger;

/**
 * IdleState is a concrete implementation of the AbstractRoboState class.
 * This class represents the idle state of the robot, where it stops all motors and waits for commands.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class IdleState extends AbstractRoboState {
  /**
   * The name of the idle state.
   */
  private static final RoboStateEnum STATE = RoboStateEnum.IDLE;

  @Override
  public RoboStateEnum getState() {
    return STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Logger.info("enter idle");
    LcdUtil.clear();
    LcdUtil.print("Idle", LcdUtil.Position.INFO);

    controller.getContext().getMotorController().stopMotors(false);
  }

  @Override
  public void onExit(RoboController controller) {
    Logger.info("exit idle");
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
  public void handleEvent(RoboController controller, AbstractEvent event) { this.handleSwitchEvent(controller, event); }
}
