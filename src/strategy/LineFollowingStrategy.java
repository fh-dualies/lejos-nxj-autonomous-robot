package strategy;

import core.RoboController;
import java.util.Objects;
import strategy.algorithm.IFollowingAlgorithm;

/**
 * LineFollowingStrategy is a concrete implementation of IDrivingStrategy that uses a following algorithm to follow a
 * line. This strategy is used to control the robot's movement based on the light sensor values. It delegates the actual
 * following logic to the provided IFollowingAlgorithm instance.
 */
public class LineFollowingStrategy implements IDrivingStrategy {
  /**
   * The IFollowingAlgorithm instance used to implement the following algorithm.
   * This instance is responsible for the actual logic of following a line.
   * Here Dependency Injection is used to provide the algorithm.
   */
  private final IFollowingAlgorithm followingAlgorithm;

  /**
   * @param followingAlgorithm The IFollowingAlgorithm instance used to implement the following algorithm.
   * @throws NullPointerException if the followingAlgorithm is null.
   */
  public LineFollowingStrategy(IFollowingAlgorithm followingAlgorithm) {
    this.followingAlgorithm = Objects.requireNonNull(followingAlgorithm);
  }

  @Override
  public void execute(RoboController controller) {
    this.followingAlgorithm.run();
  }

  @Override
  public void activate(RoboController controller) {
    this.followingAlgorithm.initialize();
  }

  @Override
  public void deactivate(RoboController controller) {
    this.followingAlgorithm.deinitialize();
  }
}
