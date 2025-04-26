package strategy;

import core.RoboController;
import io.actuator.IMotorController;
import util.Log;

public class UserControlStrategy implements IDrivingStrategy {
  private final IMotorController motorController;

  public UserControlStrategy(IMotorController motorController) {
    if (motorController == null) {
      throw new NullPointerException();
    }

    this.motorController = motorController;
  }

  @Override
  public void execute(RoboController controller) {
    // TODO: implement
  }

  @Override
  public void activate(RoboController controller) {
    Log.info("UserControlStrategy activated");

    // TODO: implement
  }

  @Override
  public void deactivate(RoboController controller) {
    Log.info("UserControlStrategy deactivated");

    // TODO: implement
  }
}
