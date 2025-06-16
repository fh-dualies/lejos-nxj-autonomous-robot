package domain.state.impl;

import core.RoboController;
import domain.event.base.AbstractEvent;
import domain.event.impl.CommandEvent;
import domain.event.impl.LineStatusEvent;
import domain.state.base.AbstractRoboState;
import domain.strategy.algorithm.PidAlgorithm;
import domain.strategy.impl.CircleSearchStrategy;
import domain.strategy.impl.LineFollowingStrategy;
import shared.constants.RoboStateEnum;
import shared.util.LcdUtil;
import shared.util.Logger;

/**
 * AutonomousState is a concrete implementation of the AbstractRoboState class.
 * This class represents the autonomous state of the robot, where it follows a line using its sensors.
 * It handles entering and exiting the state, as well as processing events that occur while in this state.
 */
public class AutonomousState extends AbstractRoboState {
  /**
   * The name of the autonomous state.
   */
  private static final RoboStateEnum STATE = RoboStateEnum.AUTONOMOUS;

  @Override
  public RoboStateEnum getState() {
    return STATE;
  }

  @Override
  public void onEnter(RoboController controller) {
    Logger.info("enter autonomous");
    LcdUtil.clear();
    LcdUtil.print("Autonomous", LcdUtil.Position.INFO);

    controller.setCurrentDrivingStrategy(new LineFollowingStrategy(new PidAlgorithm(controller)));
  }

  @Override
  public void onExit(RoboController controller) {
    Logger.info("exit autonomous");

    controller.getContext().getMotorController().stopMotors(false);
    controller.setCurrentDrivingStrategy(null);
  }

  /**
   * Handles the switch event for the autonomous state.
   * This method checks if the event is a switch event and if the target state is not autonomous.
   * If so, it calls the onExit method to exit the autonomous state.
   *
   * @param controller The RoboController instance that manages the robot's state.
   * @param event      The event that occurred.
   */
  private void handeLineStatusEvent(RoboController controller, LineStatusEvent event) {
    if (event == null || controller == null) {
      return;
    }

    boolean onLine = event.isOnLine();

    if (controller.getContext().getSensorValueStore().isOnLine() == onLine) {
      return;
    }

    if (onLine) {
      controller.setCurrentDrivingStrategy(new LineFollowingStrategy(new PidAlgorithm(controller)));
    } else {
      controller.setCurrentDrivingStrategy(new CircleSearchStrategy(controller));
    }
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
    if (event instanceof CommandEvent) {
      this.handleSwitchEvent(controller, event);
    }

    if (event instanceof LineStatusEvent) {
      this.handeLineStatusEvent(controller, (LineStatusEvent)event);
    }
  }
}
