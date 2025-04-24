package strategy;

import core.RoboController;
import io.actuator.IMotorController;
import java.util.Objects;

public class LineFollowingStrategy implements IDrivingStrategy {
  private IMotorController motorController;

  public LineFollowingStrategy(IMotorController motorController) {
    this.motorController = Objects.requireNonNull(motorController);
  }

  @Override
  public void execute(RoboController controller) {
    // TODO: implement
  }

  @Override
  public void activate(RoboController controller) {
    // TODO: implement
  }

  @Override
  public void deactivate(RoboController controller) {
    // TODO: implement
  }
}
