package io.sensor;

import event.EventManager;
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
   * @param port         The port where the light sensor is connected.
   * @param eventManager The event manager to handle events.
   */
  public LightSensorReader(SensorPort port, EventManager eventManager) {
    super(eventManager);

    if (port == null) {
      throw new NullPointerException("Sensor port cannot be null");
    }

    this.lightSensor = new LightSensor(port);
  }

  /**
   * Sets the floodlight state of the light sensor.
   *
   * @param state The state to set the floodlight to.
   */
  @Override
  public String getSensorId() {
    return SENSOR_ID;
  }

  /**
   * Sets the floodlight state of the light sensor.
   *
   * @param state The state to set the floodlight to.
   */
  @Override
  public SensorType getSensorType() {
    return SENSOR_TYPE;
  }

  /**
   * Sets the floodlight state of the light sensor.
   *
   * @param state The state to set the floodlight to.
   */
  @Override
  protected int readSensorValue() {
    return this.lightSensor.getLightValue();
  }

  /**
   * Cleans up the resources used by the light sensor.
   */
  public void close() { this.lightSensor.setFloodlight(false); }
}