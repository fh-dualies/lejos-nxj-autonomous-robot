package strategy.algorithm;

import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.DefaultSensorValues;
import util.Log;

/**
 * ZigZagAlgorithm is a concrete implementation of IFollowingAlgorithm that implements a zigzag following algorithm.
 * This algorithm is used to follow a line by adjusting the speed of the motors based on the light sensor values.
 * It alternates the speed of the left and right motors to create a zigzag pattern.
 * The algorithm assumes that the robot is searching for the line on the right side initially.
 */
public class ZigZagAlgorithm implements IFollowingAlgorithm {
  /**
   * The speed at which the robot moves forward.
   */
  private static final int FORWARD_SPEED = DefaultSensorValues.MOTOR_MEDIUM_SPEED.getValue();

  /**
   * The speed factor used for turning the robot.
   * This factor is used to adjust the speed of the outer wheel during turns.
   */
  private static final int TURN_SPEED_FACTOR = DefaultSensorValues.MOTOR_TURN_SPEED_FACTOR.getValue();

  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;

  /**
   * The motor controller used to control the motors of the robot.
   */
  private final IMotorController motorController;

  /**
   * Start by assuming the robot is searching for the line on the right side.
   */
  private boolean searchRight = true;

  /**
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the controller or motor controller is null.
   */
  public ZigZagAlgorithm(RoboController controller) {
    if (controller == null || controller.getContext().getMotorController() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.motorController = controller.getContext().getMotorController();
  }

  @Override
  public void initialize() {
    Log.info("ZigZagAlgorithm initialized");

    this.searchRight = true;
    this.motorController.stopMotors(true);
  }

  @Override
  public void deinitialize() {
    Log.info("ZigZagAlgorithm deinitialized");

    this.motorController.stopMotors(true);
  }

  @Override
  public void run() {
    int currentLightValue = this.controller.getContext().getLastLightSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    if (currentLightValue > DefaultSensorValues.LIGHT_TRANSITION_MID.getValue()) {
      if (this.searchRight) {
        int leftSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;
        int rightSpeed = FORWARD_SPEED;

        this.motorController.forward(leftSpeed, rightSpeed);
        this.searchRight = false;
      } else {
        int leftSpeed = FORWARD_SPEED;
        int rightSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;

        this.motorController.forward(leftSpeed, rightSpeed);
        this.searchRight = true;
      }
    } else {
      // TODO: slower turn?
      if (this.searchRight) {
        int leftSpeed = FORWARD_SPEED;
        int rightSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;

        this.motorController.forward(leftSpeed, rightSpeed);
      } else {
        int leftSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;
        int rightSpeed = FORWARD_SPEED;

        this.motorController.forward(leftSpeed, rightSpeed);
      }
    }
  }
}
