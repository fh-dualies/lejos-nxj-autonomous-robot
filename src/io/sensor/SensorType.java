package io.sensor;

/**
 * SensorType is an enumeration that defines the types of sensors used in the system.
 * This enumeration is used to categorize sensors and handle them appropriately in the code.
 */
public enum SensorType {
  LIGHT("LIGHT"),
  ULTRASONIC("ULTRASONIC");

  /**
   * The name of the sensor type.
   */
  private final String name;

  /**
   * @param name The name of the sensor type.
   */
  SensorType(String name) { this.name = name; }

  /**
   * @return The name of the sensor type.
   */
  public String getName() { return name; }
}
