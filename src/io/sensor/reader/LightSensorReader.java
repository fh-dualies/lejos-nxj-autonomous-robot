package io.sensor.reader;

import event.EventManager;
import io.sensor.SensorType;
import java.util.Objects;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * This class is responsible for reading the light sensor values.
 * It extends the AbstractSensorReader class and implements the required methods.
 */
public class LightSensorReader extends AbstractSensorReader {
  /**
   * The ID of the light sensor.
   */
  private static final String SENSOR_ID = "LIGHT_SENSOR_1";

  /**
   * The type of the sensor.
   */
  private static final SensorType SENSOR_TYPE = SensorType.LIGHT;

  /**
   * The light sensor instance.
   */
  private final LightSensor lightSensor;

  /**
   * @param port        The port where the light sensor is connected.
   * @param eventManager The event manager to handle events.
   */
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

  /**
   * Cleans up the resources used by the light sensor.
   */
  public void close() { this.lightSensor.setFloodlight(false); }
}