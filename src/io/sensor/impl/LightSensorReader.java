package io.sensor.impl;

import domain.event.EventManager;
import io.sensor.base.AbstractSensorReader;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import shared.constants.SensorTypeEnum;

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
  private static final SensorTypeEnum SENSOR_TYPE = SensorTypeEnum.LIGHT;

  /**
   * The light sensor instance.
   */
  private final LightSensor lightSensor;

  /**
   * Constructor for the LightSensorReader class.
   *
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
   * Gets the floodlight state of the light sensor.
   */
  @Override
  public String getSensorId() {
    return SENSOR_ID;
  }

  /**
   * Gets the type of the light sensor.
   */
  @Override
  public SensorTypeEnum getSensorType() {
    return SENSOR_TYPE;
  }

  /**
   * Reads the value from the light sensor.
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