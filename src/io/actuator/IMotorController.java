package io.actuator;

public interface IMotorController {
  void forward(int speed);

  void backward(int speed);

  void stopMotors(boolean hardStop);

  void turnLeft(int speed);

  void turnRight(int speed);
}
