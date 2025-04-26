package io.sensor.reader;

import event.EventManager;
import io.sensor.SensorType;
import java.util.Objects;
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
  private static final SensorType SENSOR_TYPE = SensorType.ULTRASONIC;

  /**
   * The UltrasonicSensor instance used to read values from the sensor.
   */
  private final UltrasonicSensor ultrasonicSensor;

  /**
   * @param port         The SensorPort where the ultrasonic sensor is connected.
   * @param eventManager The EventManager instance used to dispatch events.
   */
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