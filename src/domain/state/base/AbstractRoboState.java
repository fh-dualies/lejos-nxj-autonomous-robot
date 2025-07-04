package domain.state.base;

import core.RoboController;
import domain.command.base.ICommand;
import domain.command.impl.SwitchStateCommand;
import domain.event.base.AbstractEvent;
import domain.event.impl.CommandEvent;
import domain.state.impl.AutonomousState;
import domain.state.impl.IdleState;
import domain.state.impl.ManualState;
import shared.constants.RoboStateEnum;

/**
 * AbstractRoboState is an abstract class that defines the structure for different states of the robot.
 * Each state must implement the methods defined in this class to handle entering, exiting, and processing events.
 * This class also provides a method to handle state switching events that will be needed in all states.
 */
abstract public class AbstractRoboState implements IRoboState {
  /**
   * Handles the entry into the state.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  abstract public void onEnter(RoboController controller);

  /**
   * Handles the exit from the state.
   *
   * @param controller The RoboController instance that manages the robot's state.
   */
  abstract public void onExit(RoboController controller);

  /**
   * Handles events that occur while in the state. Called by the RoboController.
   *
   * @param controller The RoboController instance that manages the robot's state.
   * @param event      The event that occurred.
   */
  abstract public void handleEvent(RoboController controller, AbstractEvent event);

  /**
   * Returns the current state of the robot.
   *
   * @return The current state of the robot.
   */
  abstract public RoboStateEnum getState();

  /**
   * Handles state switching events. This method is called by the RoboController when a state switch event
   * occurs. It checks if the event is a SwitchStateCommand and changes the state of the controller accordingly.
   *
   * @param controller The RoboController instance that manages the robot's state.
   * @param event      The event that occurred.
   */
  protected void handleSwitchEvent(RoboController controller, AbstractEvent event) {
    if (!(event instanceof CommandEvent)) {
      return;
    }

    ICommand someCommand = ((CommandEvent)event).getCommand();

    if (!(someCommand instanceof SwitchStateCommand)) {
      return;
    }

    SwitchStateCommand command = (SwitchStateCommand)someCommand;

    RoboStateEnum targetState = command.getTargetState();
    RoboStateEnum currentState = controller.getContext().getCurrentState().getState();

    if (targetState == currentState) {
      return;
    }

    switch (targetState) {
    case IDLE:
      controller.setState(new IdleState());
      break;
    case MANUAL:
      controller.setState(new ManualState());
      break;
    case AUTONOMOUS:
      controller.setState(new AutonomousState());
      break;
    default:
      throw new IllegalStateException("Unexpected value: " + targetState);
    }
  }
}
