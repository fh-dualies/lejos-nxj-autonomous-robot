package io.sensor.reader;

import event.EventManager;
import io.sensor.SensorType;
import java.util.Objects;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightSensorReader extends AbstractSensorReader {
  private static final String SENSOR_ID = "LIGHT_SENSOR_1";
  private static final SensorType SENSOR_TYPE = SensorType.LIGHT;
  private final LightSensor lightSensor;

  public LightSensorReader(SensorPort port, EventManager eventManager) {
    super(eventManager);
    this.lightSensor = new LightSensor(Objects.requireNonNull(port));

    this.lightSensor.setFloodlight(true); // TODO: check if we have a floodlight!?
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
    return this.lightSensor.getLightValue();
  }
}