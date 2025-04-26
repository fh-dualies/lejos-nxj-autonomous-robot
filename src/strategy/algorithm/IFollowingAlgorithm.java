package strategy.algorithm;

/**
 * IFollowingAlgorithm is an interface that defines the methods for a following algorithm.
 * This interface is used to implement different following algorithms for the robot (e.g., pid or zigzag).
 */
public interface IFollowingAlgorithm {
  void initialize();

  void deinitialize();

  void run();
}
