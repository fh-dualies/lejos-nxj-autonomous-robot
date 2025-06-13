package shared.constants;

/**
 * SensorType is an enumeration that defines the types of sensors used in the system.
 * This enumeration is used to categorize sensors and handle them appropriately in the code.
 */
public enum SensorTypeEnum {
  LIGHT("LIGHT"),           // Light sensor type
  ULTRASONIC("ULTRASONIC"); // Ultrasonic sensor type

  /**
   * The name of the sensor type.
   */
  private final String name;

  /**
   * Constructor for the SensorType enumeration.
   *
   * @param name The name of the sensor type.
   */
  SensorTypeEnum(String name) { this.name = name; }

  /**
   * Returns the name of the sensor type.
   *
   * @return The name of the sensor type.
   */
  public String getName() { return name; }
}
