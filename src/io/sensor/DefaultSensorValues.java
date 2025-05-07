package io.sensor;

/**
 * DefaultSensorValues is an enumeration that defines default values for various sensors and motors.
 * These values are used as a reference for sensor readings and motor configurations.
 * The values are defined as constants to ensure consistency and avoid magic numbers in the code.
 */
public enum DefaultSensorValues {
  LIGHT_FLOOR_MIN(510f),
  LIGHT_FLOOR_MAX(600f),
  LIGHT_STRIPE_MIN(400f),
  LIGHT_STRIPE_MAX(490f),
  LIGHT_STRIPE_EDGE(50f),

  DISTANCE_STOP_THRESHOLD(25f),

  PID_KP(10f),
  PID_KI(0f),
  PID_KD(20f),

  MOTOR_MIN_SPEED(300f),
  MOTOR_MAX_SPEED(720f),
  MOTOR_ACCELERATION(4500f),
  MOTOR_TURN_SPEED_FACTOR(2f);

  /**
   * The default value for the sensor or motor.
   */
  private final float value;

  /**
   * @param value The default value for the sensor or motor.
   */
  DefaultSensorValues(float value) { this.value = value; }

  /**
   * @return as int (casts down)
   */
  public int getIntValue() { return (int)value; }

  /**
   * @return as float
   */
  public float getFloatValue() { return value; }
}