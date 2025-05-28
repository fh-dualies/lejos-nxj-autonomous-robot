package strategy.algorithm;

import core.RoboController;
import io.abstracts.IMotorController;
import io.constants.OrientationEnum;
import io.sensor.SensorValueStore;
import lejos.util.PIDController;
import main.Config;
import strategy.abstracts.IFollowingAlgorithm;
import util.Logger;

/**
 * PidAlgorithm is a concrete implementation of the IFollowingAlgorithm interface.
 * This class implements a PID controller to follow a line based on light sensor values.
 * It also handles distance sensor values to avoid collisions and adjust the robot's speed.
 */
public class PidAlgorithm implements IFollowingAlgorithm {
  /**
   * The minimum and maximum speed of the robot.
   */
  private static final int MIN_BASE_SPEED = Config.MOTOR_MIN_SPEED.getIntValue();
  private static final int MAX_BASE_SPEED = Config.MOTOR_MAX_SPEED.getIntValue();

  /**
   * The distance threshold for stopping and slowing down the robot.
   */
  private static final int STOP_DISTANCE = Config.DISTANCE_STOP_THRESHOLD.getIntValue();
  private static final int SLOW_DOWN_DISTANCE = Config.DISTANCE_SLOW_DOWN_THRESHOLD.getIntValue();

  /**
   * Proportional gain for the PID controller.
   */
  private static final float KP = Config.PID_KP.getFloatValue();

  /**
   * Integral gain for the PID controller.
   */
  private static final float KI = Config.PID_KI.getFloatValue();

  /**
   * Derivative gain for the PID controller.
   */
  private static final float KD = Config.PID_KD.getFloatValue();

  /**
   * This factor determines how much the absolute PID output (turn value)
   * reduces the base speed. A higher value means speed drops more quickly
   * with larger turns.
   */
  private static final float SPEED_REDUCTION_FACTOR_TURN = Config.SPEED_REDUCTION_FACTOR_TURN.getFloatValue();

  /**
   * This factor determines how much the speed is reduced when a collision is near.
   */
  private static final float SPEED_REDUCTION_FACTOR_COLLISION = Config.SPEED_REDUCTION_FACTOR_COLLISION.getFloatValue();

  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;

  /**
   * The motor controller used to control the motors of the robot.
   */
  private final IMotorController motorController;

  /**
   * The sensor value store used to hold the latest sensor readings and calibration values.
   */
  private final SensorValueStore sensorValueStore;

  /**
   * The PID controller used to control the robot's speed and direction.
   */
  private PIDController pidController = null;

  /**
   * Constructor for the PidAlgorithm class.
   * It initializes the PID controller with the RoboController instance.
   *
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the controller is null or if it does not have a motor controller or sensor value
   *                              store.
   */
  public PidAlgorithm(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null ||
        controller.getContext().getSensorValueStore() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.motorController = controller.getContext().getMotorController();
    this.sensorValueStore = controller.getContext().getSensorValueStore();
  }

  /**
   * Initializes the PID controller with the target light value and sets the PID parameters.
   */
  @Override
  public void initialize() {
    Logger.info("PidAlgorithm initialized");

    if (this.sensorValueStore.getLineEdgeLightValue() < 0) {
      throw new IllegalStateException("LINE_EDGE_TARGET is negative");
    }

    Logger.info("PidAlgorithm initialized with LINE_EDGE_TARGET: " + this.sensorValueStore.getLineEdgeLightValue());

    this.pidController = new PIDController(this.sensorValueStore.getLineEdgeLightValue(), 0);
    this.pidController.setPIDParam(PIDController.PID_KP, KP);
    this.pidController.setPIDParam(PIDController.PID_KI, KI);
    this.pidController.setPIDParam(PIDController.PID_KD, KD);

    this.motorController.stopMotors(true);
  }

  /**
   * Deinitializes the PID controller and stops the motors.
   */
  @Override
  public void deinitialize() {
    Logger.info("PidAlgorithm deinitialize");

    this.pidController = null;
    this.motorController.stopMotors(true);
  }

  /**
   * Runs the PID algorithm to control the robot's movement.
   * It reads the current light and distance values, calculates the turn value,
   * and adjusts the motor speeds accordingly.
   */
  @Override
  public void run() {
    if (this.pidController == null) {
      Logger.warning("PidAlgorithm not initialized");
      return;
    }

    int currentLightValue = this.sensorValueStore.getLastLightSensorValue();
    int currentDistanceValue = this.sensorValueStore.getLastDistanceSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    int turn = this.pidController.doPID(currentLightValue);
    int dynamicTargetSpeed = this.calculateDynamicTargetSpeed(turn);

    int[] speeds = this.calculateTurnSpeed(dynamicTargetSpeed, turn);

    int leftSpeed = speeds[0];
    int rightSpeed = speeds[1];

    if (currentDistanceValue == -1) {
      this.motorController.forward(leftSpeed, rightSpeed);
      return;
    }

    if (currentDistanceValue < STOP_DISTANCE) {
      this.motorController.stopMotors(true);
      return;
    }

    if (currentDistanceValue < SLOW_DOWN_DISTANCE) {
      float dynamicReduction = this.calculateDynamicReduction(currentDistanceValue);

      leftSpeed = (int)(leftSpeed * dynamicReduction);
      rightSpeed = (int)(rightSpeed * dynamicReduction);
    }

    this.motorController.forward(leftSpeed, rightSpeed);
  }

  /**
   * This method calculates the speed for the left and right motors based on the given speed and turn values.
   * It is used to adjust the speed of the motors for turning.
   *
   * @param speed The base speed for both motors.
   * @param turn  The turn value to adjust the speed of the motors.
   * @return An array containing the adjusted speeds for the left and right motors. Index 0 is the left motor speed,
   *     Index 1 is the right motor speed.
   */
  private int[] calculateTurnSpeed(int speed, int turn) {
    if (speed < 0) {
      throw new IllegalArgumentException("Speed values must be non-negative.");
    }

    // left orientation
    if (this.controller.getContext().getOrientation().equals(OrientationEnum.LEFT)) {
      return new int[] {
          speed - turn, // left motor speed
          speed + turn  // right motor speed
      };
    }

    // right orientation
    return new int[] {
        speed + turn, // left motor speed
        speed - turn  // right motor speed
    };
  }

  /**
   * Calculates the dynamic reduction factor based on the current distance value.
   * The reduction factor is calculated based on the distance to the stop threshold.
   *
   * @param currentDistanceValue The current distance value from the distance sensor.
   * @return The calculated dynamic reduction factor.
   */
  private float calculateDynamicReduction(int currentDistanceValue) {
    float range = SLOW_DOWN_DISTANCE - STOP_DISTANCE;
    float ratio = (SLOW_DOWN_DISTANCE - currentDistanceValue) / range;

    return 1f - (1f - SPEED_REDUCTION_FACTOR_COLLISION) * ratio;
  }

  /**
   * Calculates the dynamic target speed based on the turn value.
   * The speed is reduced based on the absolute value of the turn.
   *
   * @param turn The turn value from the PID controller.
   * @return The calculated dynamic target speed.
   */
  private int calculateDynamicTargetSpeed(int turn) {
    float speedReduction = Math.abs(turn) * SPEED_REDUCTION_FACTOR_TURN;

    return Math.max(MIN_BASE_SPEED, Math.min((int)(MAX_BASE_SPEED - speedReduction), MAX_BASE_SPEED));
  }
}
