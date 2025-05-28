package state.abstracts;

import core.RoboController;
import event.abstracts.AbstractEvent;
import io.constants.RoboStateEnum;

/**
 * IRoboState is an interface that defines the methods for a robot state.
 * Each state must implement the methods defined in this interface to handle entering, exiting, and processing events.
 */
public interface IRoboState {
  /**
   * This method is called when the robot enters this state.
   * It is typically used to set up any necessary resources or configurations.
   */
  void onEnter(RoboController controller);

  /**
   * This method is called when the robot exits this state.
   * It is typically used to clean up any resources or configurations used by the state.
   */
  void onExit(RoboController controller);

  /**
   * This method is called to handle events that occur while in this state.
   * It is typically used to process commands or other events relevant to the state.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   * @param event      The event that occurred.
   */
  void handleEvent(RoboController controller, AbstractEvent event);

  /**
   * Handles state switching events.
   * This method returns the current state of the robot.
   * It is typically used to check the state of the robot in various parts of the code.
   *
   * @return The current state of the robot as a RoboStateEnum.
   */
  RoboStateEnum getState();
}
