package io.actuator;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import main.Config;

/**
 * NxtMotorController is a class that controls the motors of the NXT robot.
 * It provides methods to move the robot forward, backward, turn left, turn right,
 * and stop the motors.
 */
public class NxtMotorController implements IMotorController {
  /**
   * The left motor of the NXT robot. It is connected to port A.
   */
  private final NXTRegulatedMotor leftMotor;

  /**
   * The right motor of the NXT robot. It is connected to port B.
   */
  private final NXTRegulatedMotor rightMotor;

  /**
   * Constructor for the NxtMotorController class.
   * It initializes the left and right motors and sets their speed and acceleration.
   */
  public NxtMotorController() {
    this.leftMotor = Motor.A;
    this.rightMotor = Motor.B;

    this.setInternalSpeed(Config.MOTOR_MAX_SPEED.getIntValue(), Config.MOTOR_MAX_SPEED.getIntValue());
  }

  /**
   * moves the robot forward with the specified speed.
   *
   * @param leftSpeed  The speed of the left motor.
   * @param rightSpeed The speed of the right motor.
   */
  @Override
  public void forward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.forward();
    rightMotor.forward();
  }

  /**
   * moves the robot backward with the specified speed.
   *
   * @param leftSpeed  The speed of the left motor.
   * @param rightSpeed The speed of the right motor.
   */
  @Override
  public void backward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.backward();
    rightMotor.backward();
  }

  /**
   * stops the motors of the robot.
   *
   * @param hardStop If true, the motors will stop immediately. If false, the motors will decelerate to a stop.
   */
  @Override
  public void stopMotors(boolean hardStop) {
    if (hardStop) {
      leftMotor.stop();
      rightMotor.stop();
      return;
    }

    leftMotor.flt();
    rightMotor.flt();
  }

  /**
   * Closes the motor controller and stops the motors.
   * This method is called to clean up the resources used by the motor controller.
   */
  @Override
  public void close() {
    this.stopMotors(true);
  }

  /**
   * Sets the speed and acceleration of the motors. The acceleration is set to a default value.
   *
   * @param leftSpeed  The speed of the left motor.
   * @param rightSpeed The speed of the right motor.
   */
  private void setInternalSpeed(int leftSpeed, int rightSpeed) {
    this.leftMotor.setSpeed(Math.abs(leftSpeed));
    this.rightMotor.setSpeed(Math.abs(rightSpeed));

    this.leftMotor.setAcceleration(Config.MOTOR_ACCELERATION.getIntValue());
    this.rightMotor.setAcceleration(Config.MOTOR_ACCELERATION.getIntValue());
  }
}
