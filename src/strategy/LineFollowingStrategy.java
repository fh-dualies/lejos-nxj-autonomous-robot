package strategy;

import core.RoboController;
import java.util.Objects;
import strategy.algorithm.IFollowingAlgorithm;

public class LineFollowingStrategy implements IDrivingStrategy {
  private final IFollowingAlgorithm followingAlgorithm;

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
