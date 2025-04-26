package io.sensor;

/**
 * DefaultSensorValues is an enumeration that defines default values for various sensors and motors.
 * These values are used as a reference for sensor readings and motor configurations.
 * The values are defined as constants to ensure consistency and avoid magic numbers in the code.
 */
public enum DefaultSensorValues {
  LIGHT_FLOOR_MIN(510),
  LIGHT_FLOOR_MAX(600),
  LIGHT_TRANSITION_MIN(490),
  LIGHT_TRANSITION_MID(500),
  LIGHT_TRANSITION_MAX(510),
  LIGHT_STRIPE_MIN(400),
  LIGHT_STRIPE_MAX(490),

  DISTANCE_STOP_THRESHOLD(25), // the distance at which the robot should stop

  MOTOR_MAX_SPEED(720),
  MOTOR_ACCELERATION(600),
  MOTOR_MEDIUM_SPEED(300),
  MOTOR_TURN_SPEED_FACTOR(2); // factor for outer-wheel speed in turns

  /**
   * The value of the default sensor value.
   */
  private final int value;

  /**
   * @param value The value of the default sensor value.
   */
  DefaultSensorValues(int value) { this.value = value; }

  /**
   * @return The value of the default sensor value.
   */
  public int getValue() { return value; }
}