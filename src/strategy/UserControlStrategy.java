package strategy;

import io.actuator.MotorController;
import java.util.Objects;

public class UserControlStrategy implements IDrivingStrategy {
  private MotorController motorController;

  public UserControlStrategy(MotorController motorController) {
    this.motorController = Objects.requireNonNull(motorController);
  }

  @Override
  public void execute() {
    // TODO: implement
  }
}
