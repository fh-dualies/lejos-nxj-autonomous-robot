package strategy.algorithm;

import util.Log;
import lejos.util.PIDController;
import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.DefaultSensorValues;

public class PidAlgorithm implements IFollowingAlgorithm {

  /**
   * The speed at which the robot moves forward.
   */
  private static final int TARGET_SPEED = DefaultSensorValues.MOTOR_MEDIUM_SPEED.getValue();

  /**
   * The speed factor used for turning the robot.
   * This factor is used to adjust the speed of the outer wheel during turns.
   */
  private static final int LINE_EDGE = DefaultSensorValues.LIGHT_TRANSITION_MID.getValue();

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
  private PIDController pid;

  public PidAlgorithm(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.motorController = controller.getContext().getMotorController();

    this.pid = new PIDController(LINE_EDGE, 0);
    this.pid.setPIDParam(PIDController.PID_KP, 10f);
    this.pid.setPIDParam(PIDController.PID_KI, 0f);
    this.pid.setPIDParam(PIDController.PID_KD, 20f);
  }

  @Override
  public void initialize() {
    Log.info("PidAlgorithm initialized");

    this.motorController.stopMotors(true);
  }

  @Override
  public void deinitialize() {
    Log.info("PidAlgorithm deinitialize");

    this.motorController.stopMotors(true);
  }

  @Override
  public void run() {
    int currentLightValue = this.controller.getContext().getLastLightSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    int turn = this.pid.doPID(currentLightValue);

    int leftSpeed = TARGET_SPEED + turn;
    int rightSpeed = TARGET_SPEED - turn;

    this.motorController.forward(leftSpeed, rightSpeed);
  }
}
