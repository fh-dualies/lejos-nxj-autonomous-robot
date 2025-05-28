package io.sensor;

import event.EventManager;
import io.constants.SensorTypeEnum;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * UltrasonicSensorReader is a concrete implementation of AbstractSensorReader for reading values from an ultrasonic
 * sensor. It uses the LeJOS library to interact with the ultrasonic sensor connected to a specified port.
 */
public class UltrasonicSensorReader extends AbstractSensorReader {
  /**
   * The ID of the ultrasonic sensor.
   */
  private static final String SENSOR_ID = "ULTRASONIC_SENSOR_1";

  /**
   * The type of the sensor.
   */
  private static final SensorTypeEnum SENSOR_TYPE = SensorTypeEnum.ULTRASONIC;

  /**
   * The UltrasonicSensor instance used to read values from the sensor.
   */
  private final UltrasonicSensor ultrasonicSensor;

  /**
   * Constructor for the UltrasonicSensorReader class.
   *
   * @param port         The SensorPort where the ultrasonic sensor is connected.
   * @param eventManager The EventManager instance used to dispatch events.
   */
  public UltrasonicSensorReader(SensorPort port, EventManager eventManager) {
    super(eventManager);

    if (port == null) {
      throw new NullPointerException("Sensor port cannot be null");
    }

    this.ultrasonicSensor = new UltrasonicSensor(port);
  }

  /**
   * Returns the ID of the sensor.
   *
   * @return The ID of the sensor.
   */
  @Override
  public String getSensorId() {
    return SENSOR_ID;
  }

  /**
   * Returns the type of the sensor.
   *
   * @return The type of the sensor.
   */
  @Override
  public SensorTypeEnum getSensorType() {
    return SENSOR_TYPE;
  }

  /**
   * Reads the value from the ultrasonic sensor.
   *
   * @return The distance measured by the ultrasonic sensor in centimeters.
   */
  @Override
  protected int readSensorValue() {
    return ultrasonicSensor.getDistance();
  }
}