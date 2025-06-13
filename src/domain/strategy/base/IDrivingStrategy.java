package domain.strategy.base;

import core.RoboController;

/**
 * IDrivingStrategy is an interface that defines the methods for a driving strategy.
 * This interface is used to implement different driving strategies for the robot (e.g., manual, automatic).
 */
public interface IDrivingStrategy {
  /**
   * This method is called to initialize the driving strategy.
   * It is typically used to set up any necessary resources or configurations.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  void activate(RoboController controller);

  /**
   * This method is called to execute the driving strategy.
   * It is typically used to perform the main logic of the strategy.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  void execute(RoboController controller);

  /**
   * This method is called to deactivate the driving strategy.
   * It is typically used to clean up any resources or configurations used by the strategy.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  void deactivate(RoboController controller);
}
