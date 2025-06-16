package domain.state.impl;

import core.RoboController;
import domain.event.base.AbstractEvent;
import domain.state.base.AbstractRoboState;
import domain.strategy.impl.UserControlStrategy;
import shared.constants.RoboStateEnum;
import shared.util.LcdUtil;
import shared.util.Logger;

/**
 * ManualState is a concrete implementation of the AbstractRoboState class.
 * This class represents the manual state of the robot, where it is controlled by the user.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class ManualState extends AbstractRoboState {
  /**
   * The name of the idle state.
   */
  private static final RoboStateEnum STATE = RoboStateEnum.MANUAL;

  /**
   * Constructor for the ManualState class.
   * This constructor initializes the state and sets up any necessary resources.
   */
  @Override
  public RoboStateEnum getState() {
    return STATE;
  }

  /**
   * Handles the entry into the manual state.
   * This method is called when the robot enters the manual state.
   * It initializes the state and sets up any necessary resources.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  @Override
  public void onEnter(RoboController controller) {
    Logger.info("enter manual");
    LcdUtil.clear();
    LcdUtil.print("Manual", LcdUtil.Position.INFO);

    controller.setCurrentDrivingStrategy(new UserControlStrategy(controller));
  }

  /**
   * Handles the exit from the manual state.
   * This method is called when the robot exits the manual state.
   * It cleans up any resources and stops the motors.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  @Override
  public void onExit(RoboController controller) {
    Logger.info("exit manual");

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
