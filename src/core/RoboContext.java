package core;

import event.EventManager;
import event.SensorEvent;
import io.actuator.IMotorController;
import io.connection.BluetoothTransmitter;
import io.sensor.SensorType;
import main.Config;
import state.AbstractRoboState;
import strategy.IDrivingStrategy;

/**
 * RoboContext stores all the state and configuration data for the robot.
 * It is used by the RoboController to access and modify the robot's state.
 */
public class RoboContext {
  /**
   * The event manager. This is used to dispatch events and register listeners.
   */
  private final EventManager eventManager;

  /**
   * The motor controller. This is used to control the motors of the robot.
   */
  private final IMotorController motorController;

  /**
   * The Bluetooth transmitter. This is used to send data to the remote device.
   */
  private final BluetoothTransmitter bluetoothTransmitter;

  /**
   * The last value read from the light sensor. This is used to determine the current state of the robot. It is received
   * by sensor events.
   */
  private volatile int lastLightSensorValue = -1;

  /**
   * The last value read from the distance sensor. This is used to determine the current state of the robot. It is
   * received by sensor events.
   */
  private volatile int lastDistanceSensorValue = -1;

  /**
   * The calibration value for the floor light sensor.
   */
  private volatile int floorCalibrationLightValue = Config.DEFAULT_FLOOR_LIGHT.getIntValue();

  /**
   * The calibration value for the stripe light sensor.
   */
  private volatile int stripeCalibrationLightValue = Config.DEFAULT_STRIPE_LIGHT.getIntValue();

  /**
   * The current state of the robot. This is the state that is currently active and will be called to handle incoming
   * events
   */
  private AbstractRoboState currentState = null;

  /**
   * The current driving strategy. This is the strategy that is currently active and will be called to
   */
  private IDrivingStrategy currentDrivingStrategy = null;

  /**
   * @param eventManager    The event manager used to dispatch events and register listeners.
   * @param motorController The motor controller used to control the motors of the robot.
   */
  public RoboContext(EventManager eventManager, IMotorController motorController,
                     BluetoothTransmitter bluetoothTransmitter) {
    if (eventManager == null || motorController == null) {
      throw new NullPointerException();
    }

    this.eventManager = eventManager;
    this.motorController = motorController;
    this.bluetoothTransmitter = bluetoothTransmitter;
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

    if (event.getSensorType() == SensorType.LIGHT) {
      this.lastLightSensorValue = event.getValue();
    }

    if (event.getSensorType() == SensorType.ULTRASONIC) {
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
  }

  /**
   * @return The current state of the robot.
   */
  public synchronized AbstractRoboState getCurrentState() { return this.currentState; }

  /**
   * Sets the current state of the robot.
   *
   * @param currentState The new state to set.
   */
  public synchronized void setCurrentState(AbstractRoboState currentState) { this.currentState = currentState; }

  /**
   * @return The current driving strategy.
   */
  public IDrivingStrategy getCurrentDrivingStrategy() { return this.currentDrivingStrategy; }

  /**
   * Sets the current driving strategy.
   *
   * @param strategy The new strategy to set.
   */
  public void setCurrentDrivingStrategy(IDrivingStrategy strategy) { this.currentDrivingStrategy = strategy; }

  /**
   * @return The motor controller.
   */
  public IMotorController getMotorController() { return this.motorController; }

  /**
   * @return The event manager.
   */
  public EventManager getEventManager() { return this.eventManager; }

  /**
   * @return The Bluetooth transmitter.
   */
  public BluetoothTransmitter getBluetoothTransmitter() { return this.bluetoothTransmitter; }

  /**
   * @return The last value read from the light sensor.
   */
  public int getLastLightSensorValue() { return this.lastLightSensorValue; }

  /**
   * @return The last value read from the distance sensor.
   */
  public int getLastDistanceSensorValue() { return this.lastDistanceSensorValue; }

  /**
   * @return The calibration value for the floor light sensor.
   */
  public int getFloorCalibrationLightValue() { return this.floorCalibrationLightValue; }

  /**
   * @return The calibration value for the stripe light sensor.
   */
  public int getStripeCalibrationLightValue() { return this.stripeCalibrationLightValue; }
}