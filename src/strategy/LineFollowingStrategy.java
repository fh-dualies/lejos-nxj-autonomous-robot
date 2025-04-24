package strategy;

import io.actuator.MotorController;
import java.util.Objects;

public class LineFollowingStrategy implements IDrivingStrategy {
  private MotorController motorController;

  public LineFollowingStrategy(MotorController motorController) {
    this.motorController = Objects.requireNonNull(motorController);
  }

  @Override
  public void execute() {
    // TODO: implement
  }
}
