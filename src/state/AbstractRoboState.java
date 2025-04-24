package state;

import core.RoboController;
import event.base.AbstractEvent;
import event.base.CommandEvent;
import io.command.ICommand;
import io.command.SwitchStateCommand;

abstract public class AbstractRoboState {
  abstract public void onEnter(RoboController controller);

  abstract public void onExit(RoboController controller);

  abstract public void handleEvent(RoboController controller, AbstractEvent event);

  abstract public RoboStates getState();

  protected void handleSwitchEvent(RoboController controller, AbstractEvent event) {
    if (!(event instanceof CommandEvent)) {
      return;
    }

    ICommand someCommand = ((CommandEvent)event).getCommand();

    if (!(someCommand instanceof SwitchStateCommand)) {
      return;
    }

    SwitchStateCommand command = (SwitchStateCommand)someCommand;

    RoboStates targetState = command.getTargetState();
    RoboStates currentState = controller.getCurrentState().getState();

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
