package io.actuator;

import io.sensor.DefaultSensorValues;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

/**
 * NxtMotorController is a class that controls the motors of the NXT robot.
 * It provides methods to move the robot forward, backward, turn left, turn right,
 * and stop the motors.
 * TODO: save the current speed of the motors (make it changeable via Command)
 */
public class NxtMotorController implements IMotorController {
  /*
   * The left motor of the NXT robot. It is connected to port A.
   */
  private final NXTRegulatedMotor leftMotor;

  /*
   * The right motor of the NXT robot. It is connected to port B.
   */
  private final NXTRegulatedMotor rightMotor;

  public NxtMotorController() {
    this.leftMotor = Motor.A;
    this.rightMotor = Motor.B;

    this.setInternalSpeed(DefaultSensorValues.MOTOR_MAX_SPEED.getValue(),
                          DefaultSensorValues.MOTOR_MAX_SPEED.getValue());
  }

  @Override
  public void forward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.forward();
    rightMotor.forward();
  }

  @Override
  public void backward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.backward();
    rightMotor.backward();
  }

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

  @Override
  public void turnLeft(int speed) {
    this.setInternalSpeed(speed, speed);

    leftMotor.backward();
    rightMotor.forward();
  }

  @Override
  public void turnRight(int speed) {
    this.setInternalSpeed(speed, speed);

    leftMotor.forward();
    rightMotor.backward();
  }

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
    this.leftMotor.setSpeed(Math.abs(rightSpeed));

    this.leftMotor.setAcceleration(DefaultSensorValues.MOTOR_ACCELERATION.getValue());
    this.rightMotor.setAcceleration(DefaultSensorValues.MOTOR_ACCELERATION.getValue());
  }
}
