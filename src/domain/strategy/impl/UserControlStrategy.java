package domain.strategy.impl;

import core.RoboController;
import domain.command.base.ICommand;
import domain.command.impl.MoveCommand;
import domain.event.base.AbstractEvent;
import domain.event.base.IEventListener;
import domain.event.impl.CommandEvent;
import domain.strategy.base.IDrivingStrategy;
import io.actuator.base.IMotorController;
import shared.util.Logger;

/**
 * UserControlStrategy is a driving strategy that allows the user to control the robot's motors
 * using commands received from the event manager.
 * It implements the IDrivingStrategy and IEventListener interfaces.
 */
public class UserControlStrategy implements IDrivingStrategy, IEventListener {
  /**
   * The motor controller used to control the robot's motors.
   */
  private final IMotorController motorController;

  /**
   * Constructor for the UserControlStrategy class.
   *
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the motor controller is null.
   */
  public UserControlStrategy(RoboController controller) {
    if (controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.motorController = controller.getContext().getMotorController();
  }

  /**
   * Executes the command to control the robot's motors.
   * This method is intentionally left empty as the actual command handling is done in the onEvent method.
   *
   * @param controller The RoboController instance used to control the robot.
   */
  @Override
  public void execute(RoboController controller) {
    // This method is intentionally left empty.
    // The actual command handling is done in the onEvent method.
  }

  /**
   * Activates the UserControlStrategy.
   * This method sets up the event listener for command events and logs the activation.
   *
   * @param controller The RoboController instance used to control the robot.
   */
  @Override
  public void activate(RoboController controller) {
    Logger.info("UserControlStrategy activated");

    controller.getContext().getEventManager().addListener(this);
  }

  /**
   * Deactivates the UserControlStrategy.
   * This method removes the event listener for command events and stops the motors.
   *
   * @param controller The RoboController instance used to control the robot.
   */
  @Override
  public void deactivate(RoboController controller) {
    Logger.info("UserControlStrategy deactivated");

    controller.getContext().getEventManager().removeListener(this);
    this.motorController.stopMotors(true);
  }

  /**
   * Handles command events and executes the corresponding command.
   * This method checks if the event is a CommandEvent and if the command is a MoveCommand.
   * If so, it calculates the left and right motor speeds based on the command parameters and
   * sends the command to the motor controller.
   *
   * @param event The event to handle.
   */
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

    double rad = Math.toRadians(turnAngle);
    double forward = Math.cos(rad) * speed;
    double turn = Math.sin(rad) * speed;

    int leftSpeed = (int)(forward - turn);
    int rightSpeed = (int)(forward + turn);

    this.motorController.forward(leftSpeed, rightSpeed);
  }
}
