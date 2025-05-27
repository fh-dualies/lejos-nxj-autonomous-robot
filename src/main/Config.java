package main;

/**
 * Config is an enum that contains configuration values for the robot's sensors and motors.
 * These values are used to set thresholds, PID parameters, and motor speeds.
 * The values are defined as constants and can be accessed using the enum name.
 */
public enum Config {
  LOOP_DELAY(2),
  SENSOR_REPORT_THRESHOLD(1),
  SYSTEM_MONITOR_LOG_DELAY(1000),

  DEFAULT_FLOOR_LIGHT(50f),
  DEFAULT_STRIPE_LIGHT(30f),

  MOTOR_MIN_SPEED(200f),
  MOTOR_MAX_SPEED(500f),
  MOTOR_ACCELERATION(2500f),

  // start zigzag
  LIGHT_FLOOR_MIN(510f),
  LIGHT_FLOOR_MAX(600f),
  LIGHT_STRIPE_MIN(400f),
  LIGHT_STRIPE_MAX(490f),
  LIGHT_STRIPE_EDGE(42f),
  MOTOR_TURN_SPEED_FACTOR(2f),
  // end zigzag

  // start pid
  DISTANCE_STOP_THRESHOLD(25f),
  DISTANCE_SLOW_DOWN_THRESHOLD(50f),
  SPEED_REDUCTION_FACTOR_TURN(1.25f),
  SPEED_REDUCTION_FACTOR_COLLISION(0.5f),
  PID_KP(17f),
  PID_KI(0f),
  PID_KD(20f);
  // end pid

  /**
   * The default value for the sensor or motor.
   */
  private final float value;

  /**
   * Constructor for the Config enum.
   *
   * @param value The default value for the sensor or motor.
   */
  Config(float value) { this.value = value; }

  /**
   * Returns the default value for the sensor or motor.
   *
   * @return as int (casts down)
   */
  public int getIntValue() { return (int)value; }

  /**
   * Returns the default value for the sensor or motor.
   *
   * @return as float
   */
  public float getFloatValue() { return value; }
}