package io.sensor;

public enum DefaultSensorValues {
  // light sensor values
  LIGHT_FLOOR_MIN(510),
  LIGHT_FLOOR_MAX(600),
  LIGHT_TRANSITION_MIN(490),
  LIGHT_TRANSITION_MID(500),
  LIGHT_TRANSITION_MAX(510),
  LIGHT_STRIPE_MIN(400),
  LIGHT_STRIPE_MAX(490),

  // ultrasonic sensor values
  DISTANCE_STOP_THRESHOLD(25),

  // motor values
  MOTOR_MAX_SPEED(720),
  MOTOR_ACCELERATION(600),
  MOTOR_MEDIUM_SPEED(300),
  MOTOR_TURN_SPEED_FACTOR(2); // factor for outer-wheel speed in turns

  private final int value;

  DefaultSensorValues(int value) { this.value = value; }

  public int getValue() { return value; }
}