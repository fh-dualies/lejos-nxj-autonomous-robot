package strategy;

import core.RoboController;
import event.IEventListener;
import event.base.AbstractEvent;
import event.base.CommandEvent;
import io.actuator.IMotorController;
import io.command.ICommand;
import io.command.MoveCommand;
import util.Log;

public class UserControlStrategy implements IDrivingStrategy, IEventListener {
  private final IMotorController motorController;

  public UserControlStrategy(RoboController controller) {
    if (controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.motorController = controller.getContext().getMotorController();
  }

  @Override
  public void execute(RoboController controller) {
    // This method is intentionally left empty.
    // The actual command handling is done in the onEvent method.
  }

  @Override
  public void activate(RoboController controller) {
    Log.info("UserControlStrategy activated");

    controller.getContext().getEventManager().addListener(this);
  }

  @Override
  public void deactivate(RoboController controller) {
    Log.info("UserControlStrategy deactivated");

    controller.getContext().getEventManager().removeListener(this);
    this.motorController.stopMotors(true);
  }

  @Override
  public void onEvent(AbstractEvent event) {
    if (!(event instanceof CommandEvent)) {
      return;
    }

    CommandEvent commandEvent = (CommandEvent)event;
    ICommand command = commandEvent.getCommand();

    if (!(command instanceof MoveCommand)) {
      return;
    }

    MoveCommand moveCommand = (MoveCommand)command;

    int speed = moveCommand.getSpeed();
    int turnAngle = moveCommand.getTurnAngle();

    Log.info("UserControlStrategy onEvent: speed=" + speed + ", turnAngle=" + turnAngle);
  }
}
