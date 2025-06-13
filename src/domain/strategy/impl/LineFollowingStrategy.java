package domain.strategy.impl;

import core.RoboController;
import domain.strategy.base.IDrivingStrategy;
import domain.strategy.base.IFollowingAlgorithm;
import shared.util.Logger;

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
   * Constructor for the LineFollowingStrategy class.
   *
   * @param followingAlgorithm The IFollowingAlgorithm instance used to implement the following algorithm.
   * @throws NullPointerException if the followingAlgorithm is null.
   */
  public LineFollowingStrategy(IFollowingAlgorithm followingAlgorithm) {
    if (followingAlgorithm == null) {
      throw new NullPointerException();
    }

    this.followingAlgorithm = followingAlgorithm;
  }

  @Override
  public void execute(RoboController controller) {
    this.followingAlgorithm.run();
  }

  @Override
  public void activate(RoboController controller) {
    Logger.info("LineFollowingStrategy activated");

    this.followingAlgorithm.initialize();
  }

  @Override
  public void deactivate(RoboController controller) {
    Logger.info("LineFollowingStrategy deactivated");

    this.followingAlgorithm.deinitialize();
  }
}
