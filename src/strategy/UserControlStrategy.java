package strategy;

import core.RoboController;
import io.actuator.IMotorController;
import util.Log;

public class UserControlStrategy implements IDrivingStrategy, IEventListener {
  private final IMotorController motorController;
  private int speed = 0;
  private int turnAngle = 0; // Angle in degrees

  public UserControlStrategy(RoboController controller) {
    if (controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.motorController = controller.getContext().getMotorController();
  }

  @Override
  public void execute(RoboController controller) {
    // TODO: implement
  }

  @Override
  public void activate(RoboController controller) {
    Log.info("UserControlStrategy activated");
    controller.getEventManager().addListener(this);
  }

  @Override
  public void deactivate(RoboController controller) {
    Log.info("UserControlStrategy deactivated");

    controller.getEventManager().removeListener(this);
    this.motorController.stopMotors(true);
  }

  @Override
  public void onEvent(Event event) {
    if (!(event instanceof CommandEvent)) {
      return;
    }

    CommandEvent commandEvent = (CommandEvent) event;
    ICommand command = commandEvent.getCommand();
    if (!(command instanceof MoveCommand)) {
      return;
    }

    MoveCommand moveCommand = (MoveCommand) command;
    this.speed = moveCommand.getSpeed();
    this.turnAngle = moveCommand.getTurnAngle();
  }
}
