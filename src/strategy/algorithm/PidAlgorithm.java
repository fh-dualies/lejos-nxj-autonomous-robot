package strategy.algorithm;

import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.SensorValueStore;
import lejos.util.PIDController;
import main.Config;
import util.Log;

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

  public PidAlgorithm(RoboController controller) {
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
    Log.info("PidAlgorithm initialized");

    if (this.sensorValueStore.getLineEdgeLightValue() < 0) {
      throw new IllegalStateException("LINE_EDGE_TARGET is negative");
    }

    Log.info("PidAlgorithm initialized with LINE_EDGE_TARGET: " + this.sensorValueStore.getLineEdgeLightValue());

    this.pidController = new PIDController(this.sensorValueStore.getLineEdgeLightValue(), 0);
    this.pidController.setPIDParam(PIDController.PID_KP, KP);
    this.pidController.setPIDParam(PIDController.PID_KI, KI);
    this.pidController.setPIDParam(PIDController.PID_KD, KD);

    this.motorController.stopMotors(true);
  }

  @Override
  public void deinitialize() {
    Log.info("PidAlgorithm deinitialize");

    this.pidController = null;
    this.motorController.stopMotors(true);
  }

  @Override
  public void run() {
    if (this.pidController == null) {
      Log.warning("PidAlgorithm not initialized");
      return;
    }

    int currentLightValue = this.sensorValueStore.getLastLightSensorValue();
    int currentDistanceValue = this.sensorValueStore.getLastDistanceSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    int turn = this.pidController.doPID(currentLightValue);
    int dynamicTargetSpeed = this.calculateDynamicTargetSpeed(turn);

    int leftSpeed = dynamicTargetSpeed + turn;
    int rightSpeed = dynamicTargetSpeed - turn;

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
