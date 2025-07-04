package io.actuator.base;

/**
 * Interface for controlling a motor.
 * This interface defines methods for controlling the speed and direction of a motor.
 */
public interface IMotorController {
  /**
   * Sets the speed of the left and right motors to move the robot forward.
   *
   * @param leftSpeed  The speed of the left motor.
   * @param rightSpeed The speed of the right motor.
   */
  void forward(int leftSpeed, int rightSpeed);

  /**
   * Sets the speed of the left and right motors to move the robot backward.
   *
   * @param leftSpeed  The speed of the left motor.
   * @param rightSpeed The speed of the right motor.
   */
  void backward(int leftSpeed, int rightSpeed);

  /**
   * Stops the motors of the robot.
   *
   * @param hardStop If true, the motors will stop immediately. If false, the motors will decelerate to a stop.
   */
  void stopMotors(boolean hardStop);

  /**
   * Cleans up the motor controller resources.
   */
  void close();
}
