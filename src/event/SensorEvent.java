package event;

import event.base.AbstractEvent;
import event.base.IExposableEvent;
import io.sensor.SensorType;

/**
 * SensorEvent is an event that represents a sensor reading in the system.
 * It will contain reading of the SensorReaders (see AbstractSensorReader) and represent e.g., light or distance
 * measurements. It extends the AbstractEvent class and contains a reference to the sensor ID, sensor type, and sensor
 * value.
 */
public class SensorEvent extends AbstractEvent implements IExposableEvent {
  /**
   * The ID of the sensor that generated the event.
   */
  private final String sensorId;

  /**
   * The type of the sensor that generated the event (e.g., distance, light, etc.).
   */
  private final SensorType sensorType;

  /**
   * The value of the sensor that generated the event.
   */
  private final int value;

  /**
   * Constructor for the SensorEvent class.
   *
   * @param sensorId   the ID of the sensor that generated the event
   * @param sensorType the type of the sensor that generated the event
   * @param value      the value of the sensor that generated the event
   */
  public SensorEvent(String sensorId, SensorType sensorType, int value) {
    if (sensorType == null) {
      throw new NullPointerException("Sensor type cannot be null");
    }

    if (sensorId == null || sensorId.isEmpty()) {
      throw new IllegalArgumentException("Sensor ID cannot be null or empty");
    }

    if (value < 0) {
      throw new IllegalArgumentException("Sensor value cannot be negative");
    }

    this.sensorId = sensorId;
    this.sensorType = sensorType;
    this.value = value;
  }

  /**
   * returns the sensor ID of the sensor that generated the event.
   *
   * @return the ID of the sensor that generated the event
   */
  public String getSensorId() {
    return this.sensorId;
  }

  /**
   * returns the sensor type of the sensor that generated the event.
   *
   * @return the type of the sensor that generated the event
   */
  public SensorType getSensorType() {
    return this.sensorType;
  }

  /**
   * returns the sensor value of the sensor that generated the event.
   *
   * @return the value of the sensor that generated the event
   */
  public int getValue() {
    return this.value;
  }

  /**
   * returns a string representation of the sensor event.
   *
   * @return a string representation of the sensor event
   */
  public String toExposableString() {
    return "SENSOR|" + this.sensorType.getName() + "|" + this.value;
  }
}
