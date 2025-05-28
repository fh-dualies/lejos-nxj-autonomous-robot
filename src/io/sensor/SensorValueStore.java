package io.sensor;

import event.SensorEvent;
import io.constants.SensorTypeEnum;
import java.util.Vector;
import main.Config;

/**
 * Holds the latest sensor readings, calibration values, and a self-adjusting
 * history for the light sensor to compute a dynamic-edge threshold.
 */
public final class SensorValueStore {
  /**
   * Maximum number of light readings to collect before recalculating the edge threshold.
   */
  private static final int MAX_HISTORY_SIZE = 100;

  /**
   * Allowed deviation when filtering out outlier light values.
   */
  private static final int LINE_EDGE_THRESHOLD = 5;

  /**
   * History buffer for recent light sensor values.
   */
  private final Vector<Integer> lightValueHistory = new Vector<>();

  /**
   * The last value read from the light sensor.
   */
  private volatile int lastLightSensorValue = -1;

  /**
   * The last value read from the distance sensor.
   */
  private volatile int lastDistanceSensorValue = -1;

  /**
   * Dynamic threshold for line-edge detection (average of recent valid readings).
   */
  private volatile int lineEdgeLightValue =
      (Config.DEFAULT_FLOOR_LIGHT.getIntValue() + Config.DEFAULT_STRIPE_LIGHT.getIntValue()) / 2;

  /**
   * The calibration value for the floor light sensor.
   */
  private volatile int floorCalibrationLightValue = Config.DEFAULT_FLOOR_LIGHT.getIntValue();

  /**
   * The calibration value for the stripe light sensor.
   */
  private volatile int stripeCalibrationLightValue = Config.DEFAULT_STRIPE_LIGHT.getIntValue();

  /**
   * Adds a new light reading to history if it's within the LINE_EDGE_THRESHOLD.
   * Once history reaches MAX_HISTORY_SIZE, recalculates the dynamic threshold.
   *
   * @param value latest light sensor reading
   */
  private void addLightValueToHistory(int value) {
    if (value < 0) {
      return;
    }

    if (Math.abs(value - this.lineEdgeLightValue) > LINE_EDGE_THRESHOLD) {
      return;
    }

    this.lightValueHistory.addElement(value);

    if (this.lightValueHistory.size() > MAX_HISTORY_SIZE) {
      this.generateOptimizedLightValue();
    }
  }

  /**
   * Generates the optimized light value for the line edge. This is used to determine the current state of the robot.
   * It is received by sensor events.
   */
  private void generateOptimizedLightValue() {
    if (this.lightValueHistory.isEmpty()) {
      return;
    }

    int sum = 0;

    for (int i = 0; i < this.lightValueHistory.size(); i++) {
      sum += this.lightValueHistory.elementAt(i);
    }

    this.lineEdgeLightValue = sum / this.lightValueHistory.size();
    this.lightValueHistory.clear();
  }

  /**
   * Updates the sensor values based on a sensor event.
   *
   * @param event The sensor event containing new sensor data.
   */
  public void updateFromSensorEvent(SensorEvent event) {
    if (event == null) {
      throw new NullPointerException();
    }

    if (event.getSensorType() == SensorTypeEnum.LIGHT) {
      int value = event.getValue();

      this.lastLightSensorValue = value;
      this.addLightValueToHistory(value);
    }

    if (event.getSensorType() == SensorTypeEnum.ULTRASONIC) {
      this.lastDistanceSensorValue = event.getValue();
    }
  }

  /**
   * Updates the calibration values for the light sensors.
   *
   * @param floorLightValue  The new calibration value for the floor light sensor.
   * @param stripeLightValue The new calibration value for the stripe light sensor.
   */
  public void updateCalibrationValues(int floorLightValue, int stripeLightValue) {
    if (floorLightValue < 0 || stripeLightValue < 0) {
      throw new IllegalArgumentException("Light values must be non-negative.");
    }

    this.floorCalibrationLightValue = floorLightValue;
    this.stripeCalibrationLightValue = stripeLightValue;

    this.lineEdgeLightValue = (this.floorCalibrationLightValue + this.stripeCalibrationLightValue) / 2;
  }

  /**
   * Returns the last value read from the light sensor.
   *
   * @return The last value read from the light sensor.
   */
  public int getLastLightSensorValue() { return this.lastLightSensorValue; }

  /**
   * Returns the last value read from the distance sensor.
   *
   * @return The last value read from the distance sensor.
   */
  public int getLastDistanceSensorValue() { return this.lastDistanceSensorValue; }

  /**
   * Returns the calibration value for the floor light sensor.
   *
   * @return The calibration value for the floor light sensor.
   */
  public int getFloorCalibrationLightValue() { return this.floorCalibrationLightValue; }

  /**
   * Returns the calibration value for the stripe light sensor.
   *
   * @return The calibration value for the stripe light sensor.
   */
  public int getStripeCalibrationLightValue() { return this.stripeCalibrationLightValue; }

  /**
   * Returns the self-optimizing light value for the line edge.
   *
   * @return The self-optimizing light value for the line edge.
   */
  public int getLineEdgeLightValue() { return this.lineEdgeLightValue; }
}
