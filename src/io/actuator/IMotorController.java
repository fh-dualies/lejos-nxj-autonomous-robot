package io.actuator;

public interface IMotorController {
  void forward(int leftSpeed, int rightSpeed);

  void backward(int leftSpeed, int rightSpeed);

  void stopMotors(boolean hardStop);

  void turnLeft(int speed);

  void turnRight(int speed);

  void close();
}
