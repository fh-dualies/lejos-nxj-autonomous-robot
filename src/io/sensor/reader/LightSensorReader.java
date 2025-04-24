package io.sensor.reader;

import event.EventManager;
import event.base.SensorEvent;
import io.sensor.SensorType;
import java.util.Objects;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightSensorReader implements ISensorReader {
  private static final String SENSOR_ID = "LIGHT_SENSOR_1";
  private static final SensorType SENSOR_TYPE = SensorType.LIGHT;

  private final LightSensor lightSensor;
  private final EventManager eventManager;
  private int lastValue = -1;

  public LightSensorReader(SensorPort port, EventManager eventManager) {
    this.lightSensor = new LightSensor(Objects.requireNonNull(port));
    this.eventManager = Objects.requireNonNull(eventManager);

    this.lightSensor.setFloodlight(true); // TODO: check if we have a floodlight!?
  }

  public String getSensorId() {
    return SENSOR_ID;
  }

  public SensorType getSensorType() {
    return SENSOR_TYPE;
  }

  public int checkValue() {
    int currentValue = lightSensor.getLightValue();
    int reportThreshold = 10;

    if (this.lastValue == -1 || Math.abs(currentValue - this.lastValue) >= reportThreshold) {
      this.lastValue = currentValue;
      eventManager.dispatch(new SensorEvent(SENSOR_ID, SENSOR_TYPE, currentValue));
    }

    return currentValue;
  }

  public int getLastValue() {
    return this.lastValue;
  }
}
