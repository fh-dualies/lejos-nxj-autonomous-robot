package strategy;

import core.RoboController;
import io.abstracts.IMotorController;
import io.sensor.SensorValueStore;
import main.Config;
import strategy.abstracts.IDrivingStrategy;
import util.Logger;

/**
 * CircleSearchAlgorithm is a concrete implementation of the IFollowingAlgorithm interface.
 * This class represents a circle search algorithm used to find a line by moving in circles.
 * It handles the initialization, deinitialization, and execution of the algorithm.
 */
public class CircleSearchStrategy implements IDrivingStrategy {
  /**
   * The maximum search radius for the circle search algorithm.
   */
  private static final int MAX_SEARCH_RADIUS = 5;

  /**
   * The initial duration for the first circle search radius.
   */
  private static final int INITIAL_RADIUS_DURATION_MS = 1000;

  /**
   * The increment in duration for each later circle search radius.
   */
  private static final int RADIUS_INCREMENT_MS = 500;

  /**
   * The default tolerance for light value comparison.
   */
  private static final int DEFAULT_TOLERANCE = 2;

  /**
   * The waiting period in milliseconds before increasing the search radius.
   */
  private static final int waitingPeriodMs = 200;

  /**
   * The motor controller used to control the motors of the robot.
   */
  private final IMotorController motorController;

  /**
   * The sensor value store used to access the latest sensor readings.
   */
  private final SensorValueStore sensorValueStore;

  /**
   * The current radius of the circle being searched.
   */
  private int currentRadius = 0;

  /**
   * The start time of the current step in the circle search.
   */
  private long currentStepStartTime = 0;

  /**
   * Indicates whether the algorithm is currently waiting before increasing the search radius.
   */
  private boolean isWaiting = false;

  /**
   * The start time of the waiting period before increasing the search radius.
   */
  private long waitStartTime = 0;

  /**
   * Constructor for the CircleSearchAlgorithm class.
   *
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the motor controller or sensor value store is null.
   */
  public CircleSearchStrategy(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null ||
        controller.getContext().getSensorValueStore() == null) {
      throw new NullPointerException();
    }

    this.motorController = controller.getContext().getMotorController();
    this.sensorValueStore = controller.getContext().getSensorValueStore();
  }

  /**
   * Activate the CircleSearchAlgorithm.
   * This method sets up the initial state and prepares the robot for the search.
   */
  @Override
  public void activate(RoboController controller) {
    Logger.info("CircleSearchStrategy initialized");

    this.currentRadius = 0;
    this.currentStepStartTime = System.currentTimeMillis();
    this.isWaiting = false;
    this.waitStartTime = 0;

    motorController.stopMotors(true);
  }

  /**
   * Deactivate the CircleSearchAlgorithm.
   * This method cleans up the state and stops the motors.
   */
  @Override
  public void deactivate(RoboController controller) {
    Logger.info("CircleSearchAlgorithm deinitialize");

    motorController.stopMotors(true);
  }

  /**
   * Runs the CircleSearchAlgorithm.
   * This method executes the logic of the circle search strategy, moving in circles and checking for the line.
   */
  @Override
  public void execute(RoboController controller) {
    if (this.currentRadius >= MAX_SEARCH_RADIUS) {
      Logger.info("[MAX REACHED] Line could not be found in Circle Search Strategy.");
      return;
    }

    long now = System.currentTimeMillis();

    if (this.isWaiting) {
      if (now - this.waitStartTime >= waitingPeriodMs) {
        this.isWaiting = false;
        this.currentRadius++;
        this.currentStepStartTime = now;
      }

      return;
    }

    int duration = INITIAL_RADIUS_DURATION_MS + this.currentRadius * RADIUS_INCREMENT_MS;

    if (now - this.currentStepStartTime < duration) {
      this.motorController.forward(Config.MOTOR_MIN_SPEED.getIntValue(), Config.MOTOR_MAX_SPEED.getIntValue());

      int lightValue = this.sensorValueStore.getLastLightSensorValue();

      if (lightValue >= 0 &&
          Math.abs(lightValue - this.sensorValueStore.getLineEdgeLightValue()) <= DEFAULT_TOLERANCE) {
        Logger.info("Line found in Circle No.: " + currentRadius);
      }
    } else {
      this.motorController.stopMotors(true);
      this.isWaiting = true;
      this.waitStartTime = now;
    }
  }
}
