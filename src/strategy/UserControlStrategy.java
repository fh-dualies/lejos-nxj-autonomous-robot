package strategy;

import io.actuator.IMotorController;
import java.util.Objects;

public class UserControlStrategy implements IDrivingStrategy {
  private IMotorController motorController;

  public UserControlStrategy(IMotorController motorController) {
    this.motorController = Objects.requireNonNull(motorController);
  }

  @Override
  public void execute() {
    // TODO: implement
  }
}
