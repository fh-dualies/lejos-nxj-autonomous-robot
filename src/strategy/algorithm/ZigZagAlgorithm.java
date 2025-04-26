package strategy.algorithm;

import core.RoboController;
import io.actuator.IMotorController;
import io.sensor.DefaultSensorValues;
import java.util.Objects;

public class ZigZagAlgorithm implements IFollowingAlgorithm {
  private final RoboController controller;
  private final IMotorController motorController;

  private static final int FORWARD_SPEED = DefaultSensorValues.MOTOR_MEDIUM_SPEED.getValue();
  private static final int TURN_SPEED_FACTOR = DefaultSensorValues.MOTOR_TURN_SPEED_FACTOR.getValue();

  /**
   * Start by assuming the robot is searching for the line on the right side.
   */
  private boolean searchRight = true;

  public ZigZagAlgorithm(RoboController controller) {
    this.controller = Objects.requireNonNull(controller);
    this.motorController = Objects.requireNonNull(controller.getMotorController());
  }

  @Override
  public void initialize() {
    System.out.println("ZigZagAlgorithm initialized");

    this.searchRight = true;
    this.motorController.stopMotors(true);
  }

  @Override
  public void deinitialize() {
    this.motorController.stopMotors(true);
  }

  @Override
  public void run() {
    int currentLightValue = this.controller.getLastLightSensorValue();

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
