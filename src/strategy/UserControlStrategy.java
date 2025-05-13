package strategy;

import core.RoboController;
import event.CommandEvent;
import event.base.AbstractEvent;
import event.base.IEventListener;
import io.actuator.IMotorController;
import io.command.ICommand;
import io.command.MoveCommand;
import util.Log;

public class UserControlStrategy implements IDrivingStrategy, IEventListener {
  /**
   * The motor controller used to control the robot's motors.
   */
  private final IMotorController motorController;

  /**
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the motor controller is null.
   */
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

    int leftSpeed = speed + turnAngle;
    int rightSpeed = speed - turnAngle;

    this.motorController.forward(leftSpeed, rightSpeed);
  }
}
