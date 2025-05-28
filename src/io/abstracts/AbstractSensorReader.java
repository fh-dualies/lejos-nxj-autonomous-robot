package io.abstracts;

import event.EventManager;
import event.SensorEvent;
import io.constants.SensorTypeEnum;
import main.Config;

/**
 * Abstract class for reading sensor values.
 * This class provides a template for reading sensor values and dispatching events when the value changes significantly.
 * It uses an EventManager to dispatch events and keeps track of the last reported value.
 */
public abstract class AbstractSensorReader {
  /**
   * The default threshold for reporting changes in sensor values.
   * If the change in value is greater than or equal to this threshold, an event will be dispatched.
   */
  protected static final int DEFAULT_REPORT_THRESHOLD = Config.SENSOR_REPORT_THRESHOLD.getIntValue();

  /**
   * The EventManager instance used to dispatch events.
   */
  private final EventManager eventManager;

  /**
   * The last reported value of the sensor.
   * This is used to determine if the current value is significantly different from the last reported value.
   */
  private int lastValue = -1;

  /**
   * Constructor that initializes the AbstractSensorReader with an EventManager instance.
   *
   * @param eventManager The EventManager instance used to dispatch events.
   */
  public AbstractSensorReader(EventManager eventManager) {
    if (eventManager == null) {
      throw new NullPointerException();
    }

    this.eventManager = eventManager;
  }

  /**
   * Checks the current value of the sensor and dispatches an event if the value has changed significantly.
   * The method compares the current value with the last reported value and checks if the change is greater than or
   * equal to the threshold. If so, it updates the last reported value and dispatches a SensorEvent.
   *
   * @return The current value of the sensor.
   */
  public int checkValue() {
    int currentValue = this.readSensorValue();

    if (this.lastValue == -1 || Math.abs(currentValue - this.lastValue) >= this.getReportThreshold()) {
      this.lastValue = currentValue;
      this.eventManager.dispatch(new SensorEvent(this.getSensorId(), this.getSensorType(), currentValue));
    }

    return currentValue;
  }

  /**
   * Returns the last reported value of the sensor.
   *
   * @return The last reported value of the sensor.
   */
  public int getLastValue() { return this.lastValue; }

  /**
   * Returns the EventManager instance used to dispatch events.
   *
   * @return The ID of the sensor.
   */
  abstract String getSensorId();

  /**
   * Returns the type of the sensor.
   *
   * @return The type of the sensor.
   */
  abstract SensorTypeEnum getSensorType();

  /**
   * Reads the current value of the sensor.
   * This method should be implemented by subclasses to provide the specific logic for reading the sensor value.
   *
   * @return The current value of the sensor.
   */
  protected abstract int readSensorValue();

  /**
   * This method can be overridden by subclasses to provide a custom threshold.
   *
   * @return The threshold for reporting changes in sensor values.
   */
  protected int getReportThreshold() { return DEFAULT_REPORT_THRESHOLD; }
}