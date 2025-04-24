package event.base;

import io.sensor.SensorType;
import java.util.Objects;

public class SensorEvent extends AbstractEvent {
  private final String sensorId;
  private final SensorType sensorType;
  private final int value;

  public SensorEvent(String sensorId, SensorType sensorType, int value) {
    super();

    if (sensorId == null || sensorId.isEmpty()) {
      throw new IllegalArgumentException("Sensor ID cannot be null or empty");
    }

    if (value < 0) {
      throw new IllegalArgumentException("Sensor value cannot be negative");
    }

    this.sensorId = sensorId;
    this.sensorType = Objects.requireNonNull(sensorType, "Sensor type cannot be null");
    this.value = value;
  }

  public String getSensorId() { return this.sensorId; }

  public SensorType getSensorType() { return this.sensorType; }

  public int getValue() { return this.value; }

  @Override
  public String toString() {
    return "SensorEvent{"
        + "sensorId='" + this.sensorId + '\'' + ", sensorType=" + this.sensorType + ", value=" + this.value +
        ", timestamp=" + this.getTimestamp() + '}';
  }
}
