package strategy.algorithm;

import core.RoboController;
import io.abstracts.IMotorController;
import main.Config;
import strategy.abstracts.IFollowingAlgorithm;
import util.Log;

/**
 * ZigZagAlgorithm is a concrete implementation of IFollowingAlgorithm that implements a zigzag-following algorithm.
 * This algorithm is used to follow a line by adjusting the speed of the motors based on the light sensor values.
 * It alternates the speed of the left and right motors to create a zigzag pattern.
 * The algorithm assumes that the robot is searching for the line on the right side initially.
 */
public class ZigZagAlgorithm implements IFollowingAlgorithm {
  /**
   * The speed at which the robot moves forward.
   */
  private static final int FORWARD_SPEED = Config.MOTOR_MIN_SPEED.getIntValue();

  /**
   * The speed factor used for turning the robot.
   * This factor is used to adjust the speed of the outer wheel during turns.
   */
  private static final int TURN_SPEED_FACTOR = Config.MOTOR_TURN_SPEED_FACTOR.getIntValue();

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
   * Constructor for the ZigZagAlgorithm class.
   *
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

  /**
   * Initializes the ZigZagAlgorithm.
   * This method is called when the algorithm is activated.
   * It sets the initial state of the robot and stops the motors.
   */
  @Override
  public void initialize() {
    Log.info("ZigZagAlgorithm initialized");

    this.searchRight = true;
    this.motorController.stopMotors(true);
  }

  /**
   * Deinitializes the ZigZagAlgorithm.
   * This method is called when the algorithm is deactivated.
   * It stops the motors and resets the state of the robot.
   */
  @Override
  public void deinitialize() {
    Log.info("ZigZagAlgorithm deinitialized");

    this.motorController.stopMotors(true);
  }

  /**
   * Executes the zigzag following algorithm.
   * This method is called periodically to adjust the speed of the motors based on the light sensor values.
   * It alternates the speed of the left and right motors to create a zigzag pattern.
   */
  @Override
  public void run() {
    int currentLightValue = this.controller.getContext().getSensorValueStore().getLastLightSensorValue();

    if (currentLightValue == -1) {
      return;
    }

    if (currentLightValue > Config.LIGHT_STRIPE_EDGE.getIntValue()) {
      if (this.searchRight) {
        int leftSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;

        this.motorController.forward(leftSpeed, FORWARD_SPEED);
        this.searchRight = false;
      } else {
        int rightSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;

        this.motorController.forward(FORWARD_SPEED, rightSpeed);
        this.searchRight = true;
      }
    } else {
      int rightSpeed = FORWARD_SPEED / TURN_SPEED_FACTOR;
      if (this.searchRight) {

        this.motorController.forward(FORWARD_SPEED, rightSpeed);
      } else {

        this.motorController.forward(rightSpeed, FORWARD_SPEED);
      }
    }
  }
}
