package strategy;

import io.actuator.IMotorController;
import java.util.Objects;

public class LineFollowingStrategy implements IDrivingStrategy {
  private IMotorController motorController;

  public LineFollowingStrategy(IMotorController motorController) {
    this.motorController = Objects.requireNonNull(motorController);
  }

  @Override
  public void execute() {
    // TODO: implement
  }
}
