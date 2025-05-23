package strategy.algorithm;

import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.SensorValueStore;
import main.Config;
import util.Log;

/**
 * CircleSearchAlgorithm is a concrete implementation of the IFollowingAlgorithm interface.
 * This class represents a circle search algorithm used to find a line by moving in circles.
 * It handles the initialization, deinitialization, and execution of the algorithm.
 */
public class CircleSearchAlgorithm implements IFollowingAlgorithm {
  /**
   * The maximum search radius for the circle search algorithm.
   */
  private static final int MAX_SEARCH_RADIUS = 5;
  /**
   * The initial duration for the first circle search radius.
   */
  private static final int INITIAL_RADIUS_DURATION_MS = 1000;
  /**
   * The increment in duration for each subsequent circle search radius.
   */
  private static final int RADIUS_INCREMENT_MS = 500;
  /**
   * The default tolerance for light value comparison.
   */
  private static final int DEFAULT_TOLERANCE = 2;
  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;
  /**
   * The motor controller used to control the motors of the robot.
   */
  private final IMotorController motorController;
  /**
   * The sensor value store used to access the latest sensor readings.
   */
  private final SensorValueStore sensorValueStore;

  /**
   * Constructor for the CircleSearchAlgorithm class.
   *
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the motor controller or sensor value store is null.
   */
  public CircleSearchAlgorithm(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null ||
        controller.getContext().getSensorValueStore() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.motorController = controller.getContext().getMotorController();
    this.sensorValueStore = controller.getContext().getSensorValueStore();
  }

  @Override
  public void initialize() {
    Log.info("CircleSearchAlgorithm initialized");
    motorController.stopMotors(true);
  }

  @Override
  public void deinitialize() {
    Log.info("CircleSearchAlgorithm deinitialize");
    motorController.stopMotors(true);
  }

  @Override
  public void run() {
    for (int i = 0; i < MAX_SEARCH_RADIUS; i++) {
      int duration = INITIAL_RADIUS_DURATION_MS + i * RADIUS_INCREMENT_MS;
      long startTime = System.currentTimeMillis();

      while (System.currentTimeMillis() - startTime < duration) {
        motorController.forward(Config.MOTOR_MIN_SPEED.getIntValue(), Config.MOTOR_MAX_SPEED.getIntValue());
        int lightValue = sensorValueStore.getLastLightSensorValue();
        if (lightValue >= 0 && Math.abs(lightValue - sensorValueStore.getLineEdgeLightValue()) <= DEFAULT_TOLERANCE) {
          Log.info("Line found in Circle No.: " + i);
          return;
        }

        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          // schlucken
        }
      }

      motorController.stopMotors(true);
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        // schlucken
      }
    }

    Log.info("Line could not be found in Circle Search Algorithm.");
  }
}
