package domain.state.impl;

import core.RoboController;
import domain.event.base.AbstractEvent;
import domain.state.base.AbstractRoboState;
import domain.strategy.impl.CalibrationStrategy;
import shared.constants.RoboStateEnum;
import shared.util.LcdUtil;

/**
 * CalibrationState is a concrete implementation of the AbstractRoboState class.
 * This class represents the calibration state of the robot, where it calibrates its sensors.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class CalibrationState extends AbstractRoboState {
  /**
   * The name of the calibration state.
   */
  private static final RoboStateEnum STATE = RoboStateEnum.CALIBRATION;

  /**
   * The name of the calibration state.
   */
  @Override
  public RoboStateEnum getState() {
    return STATE;
  }

  /**
   * Handles the entry into the calibration state.
   * This method clears the LCD screen and stops the motors.
   * It also sets the current driving strategy to a new CalibrationStrategy instance.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  @Override
  public void onEnter(RoboController controller) {
    LcdUtil.clear();

    controller.getContext().getMotorController().stopMotors(true);
    controller.setCurrentDrivingStrategy(new CalibrationStrategy(controller));
  }

  /**
   * Handles the exit from the calibration state.
   * This method clears the LCD screen and stops the motors.
   * It also sets the current driving strategy to null.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  @Override
  public void onExit(RoboController controller) {
    LcdUtil.clear();

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
