package io.sensor.base;

import shared.constants.SensorTypeEnum;

/**
 * ISensorReader is an interface that defines methods for reading sensor values.
 * It provides methods to check the current value, get the last value, retrieve the sensor ID,
 * and determine the type of sensor.
 */
public interface ISensorReader {
  /**
   * Checks the current value of the sensor and dispatches an domain.event if the value has changed significantly.
   * The method compares the current value with the last reported value and checks if the change is greater than or
   * equal to the threshold. If so, it updates the last reported value and dispatches a SensorEvent.
   *
   * @return The current value of the sensor.
   */
  int checkValue();

  /**
   * Gets the threshold for reporting changes in sensor values.
   * If the change in value is greater than or equal to this threshold, an domain.event will be dispatched.
   *
   * @return The threshold for reporting changes in sensor values.
   */
  int getLastValue();

  /**
   * Gets the ID of the sensor.
   *
   * @return The ID of the sensor.
   */
  String getSensorId();

  /**
   * Gets the type of the sensor.
   *
   * @return The type of the sensor.
   */
  SensorTypeEnum getSensorType();
}
