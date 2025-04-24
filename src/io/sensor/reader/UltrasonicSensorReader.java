package io.sensor.reader;

import event.EventManager;
import io.sensor.SensorType;
import java.util.Objects;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicSensorReader extends AbstractSensorReader {
  private static final String SENSOR_ID = "ULTRASONIC_SENSOR_1";
  private static final SensorType SENSOR_TYPE = SensorType.ULTRASONIC;
  private final UltrasonicSensor ultrasonicSensor;

  public UltrasonicSensorReader(SensorPort port, EventManager eventManager) {
    super(eventManager);
    this.ultrasonicSensor = new UltrasonicSensor(Objects.requireNonNull(port));
  }

  @Override
  public String getSensorId() {
    return SENSOR_ID;
  }

  @Override
  public SensorType getSensorType() {
    return SENSOR_TYPE;
  }

  @Override
  protected int readSensorValue() {
    return ultrasonicSensor.getDistance();
  }
}