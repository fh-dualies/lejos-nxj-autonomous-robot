package core;

import event.EventManager;
import io.actuator.IMotorController;
import io.connection.BluetoothTransmitter;
import io.sensor.SensorValueStore;
import state.AbstractRoboState;
import strategy.IDrivingStrategy;

/**
 * RoboContext stores all the state and configuration data for the robot.
 * It is used by the RoboController to access and modify the robot's state.
 */
public final class RoboContext {
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
   * Store for sensor values. This is used to store the last values read from the sensors.
   */
  private final SensorValueStore sensorValueStore;

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
   * Constructor for the RoboContext class.
   *
   * @param eventManager    The event manager used to dispatch events and register listeners.
   * @param motorController The motor controller used to control the motors of the robot.
   */
  public RoboContext(EventManager eventManager, IMotorController motorController,
                     BluetoothTransmitter bluetoothTransmitter, SensorValueStore sensorValueStore) {
    if (eventManager == null || motorController == null) {
      throw new NullPointerException();
    }

    this.eventManager = eventManager;
    this.motorController = motorController;
    this.bluetoothTransmitter = bluetoothTransmitter;
    this.sensorValueStore = sensorValueStore;
  }

  /**
   * Returns the Current state of the robot.
   *
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
   * Returns the current driving strategy.
   *
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
   * Returns the motor controller.
   *
   * @return The motor controller.
   */
  public IMotorController getMotorController() { return this.motorController; }

  /**
   * Returns the event manager.
   *
   * @return The event manager.
   */
  public EventManager getEventManager() { return this.eventManager; }

  /**
   * Returns the Bluetooth transmitter.
   *
   * @return The Bluetooth transmitter.
   */
  public BluetoothTransmitter getBluetoothTransmitter() { return this.bluetoothTransmitter; }

  /**
   * Returns the sensor value store.
   *
   * @return The sensor value store.
   */
  public SensorValueStore getSensorValueStore() { return this.sensorValueStore; }
}