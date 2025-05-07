package strategy.algorithm;

import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.DefaultSensorValues;
import lejos.util.PIDController;
import util.Log;

public class PidAlgorithm implements IFollowingAlgorithm {
  /**
   * The minimum and maximum speed of the robot.
   */
  private static final int MIN_BASE_SPEED = DefaultSensorValues.MOTOR_MIN_SPEED.getIntValue();
  private static final int MAX_BASE_SPEED = DefaultSensorValues.MOTOR_MAX_SPEED.getIntValue();

  /**
   * The speed factor used for turning the robot.
   * This factor is used to adjust the speed of the outer wheel during turns.
   */
  private static final int LINE_EDGE_TARGET = DefaultSensorValues.LIGHT_STRIPE_EDGE.getIntValue();

  /**
   * Proportional gain for the PID controller.
   */
  private static final float KP = DefaultSensorValues.PID_KP.getFloatValue();

  /**
   * Integral gain for the PID controller.
   */
  private static final float KI = DefaultSensorValues.PID_KI.getFloatValue();

  /**
   * Derivative gain for the PID controller.
   */
  private static final float KD = DefaultSensorValues.PID_KD.getFloatValue();

  /**
   * This factor determines how much the absolute PID output (turn value)
   * reduces the base speed. A higher value means speed drops more quickly
   * with larger turns. TODO: This needs tuning
   */
  private static final float SPEED_REDUCTION_FACTOR_PER_TURN_UNIT = 1.5f;

  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;

  /**
   * The motor controller used to control the motors of the robot.
   */
  private final IMotorController motorController;

  /**
   * The PID controller used to control the robot's speed and direction.
   */
  private PIDController pidController = null;

  public PidAlgorithm(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.motorController = controller.getContext().getMotorController();
  }

  @Override
  public void initialize() {
    Log.info("PidAlgorithm initialized");

    this.pidController = new PIDController(LINE_EDGE_TARGET, 0);
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

    int currentLightValue = this.controller.getContext().getLastLightSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    int turn = this.pidController.doPID(currentLightValue);

    // TODO: check if this works at all
    float speedReduction = Math.abs(turn) * SPEED_REDUCTION_FACTOR_PER_TURN_UNIT;
    int dynamicTargetSpeed = Math.max(MIN_BASE_SPEED, Math.min((int)(MAX_BASE_SPEED - speedReduction), MAX_BASE_SPEED));

    int leftSpeed = dynamicTargetSpeed + turn;
    int rightSpeed = dynamicTargetSpeed - turn;

    this.motorController.forward(leftSpeed, rightSpeed);
  }
}
