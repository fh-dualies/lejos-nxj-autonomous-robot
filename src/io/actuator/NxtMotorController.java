package io.actuator;

import io.sensor.DefaultSensorValues;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class NxtMotorController implements IMotorController {
  private final NXTRegulatedMotor leftMotor;
  private final NXTRegulatedMotor rightMotor;

  // TODO: save the current speed of the motors (make it changeable via Command)

  public NxtMotorController() {
    this.leftMotor = Motor.A;
    this.rightMotor = Motor.B;

    this.setInternalSpeed(DefaultSensorValues.MOTOR_MAX_SPEED.getValue(),
                          DefaultSensorValues.MOTOR_MAX_SPEED.getValue());
  }

  public void forward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.forward();
    rightMotor.forward();
  }

  public void backward(int leftSpeed, int rightSpeed) {
    this.setInternalSpeed(leftSpeed, rightSpeed);

    leftMotor.backward();
    rightMotor.backward();
  }

  public void stopMotors(boolean hardStop) {
    if (hardStop) {
      leftMotor.stop();
      rightMotor.stop();
      return;
    }

    leftMotor.flt();
    rightMotor.flt();
  }

  public void turnLeft(int speed) {
    this.setInternalSpeed(speed, speed);

    leftMotor.backward();
    rightMotor.forward();
  }

  public void turnRight(int speed) {
    this.setInternalSpeed(speed, speed);

    leftMotor.forward();
    rightMotor.backward();
  }

  private void setInternalSpeed(int leftSpeed, int rightSpeed) {
    this.leftMotor.setSpeed(Math.abs(leftSpeed));
    this.leftMotor.setSpeed(Math.abs(rightSpeed));

    this.leftMotor.setAcceleration(DefaultSensorValues.MOTOR_ACCELERATION.getValue());
    this.rightMotor.setAcceleration(DefaultSensorValues.MOTOR_ACCELERATION.getValue());
  }

  public void close() { this.stopMotors(true); }
}
