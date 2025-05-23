package strategy.algorithm;

/**
 * IFollowingAlgorithm is an interface that defines the methods for a following algorithm.
 * This interface is used to implement the different following algorithms for the robot (e.g., pid or zigzag).
 */
public interface IFollowingAlgorithm {
  /**
   * The RoboController instance used to control the robot.
   */
  void initialize();

  /**
   * The RoboController instance used to control the robot.
   */
  void deinitialize();

  /**
   * The RoboController instance used to control the robot.
   */
  void run();
}
