package core;

import domain.event.EventManager;
import domain.state.base.AbstractRoboState;
import domain.strategy.base.IDrivingStrategy;
import io.actuator.base.IMotorController;
import io.connection.impl.BluetoothTransmitter;
import io.sensor.SensorValueStore;
import shared.constants.OrientationEnum;

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
   * The current orientation of the robot. This is used to determine the direction the robot is facing.
   */
  private OrientationEnum orientation = OrientationEnum.LEFT;

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

  /**
   * Returns the current orientation of the robot.
   *
   * @return The current orientation of the robot.
   */
  public OrientationEnum getOrientation() { return this.orientation; }

  /**
   * Sets the current orientation of the robot.
   *
   * @param orientation The new orientation to set.
   */
  public void setOrientation(OrientationEnum orientation) {
    if (orientation == null) {
      throw new NullPointerException("Orientation cannot be null");
    }

    this.orientation = orientation;
  }
}