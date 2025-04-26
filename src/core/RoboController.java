package core;

import event.EventManager;
import event.IEventListener;
import event.base.AbstractEvent;
import event.base.SensorEvent;
import io.actuator.IMotorController;
import io.sensor.SensorType;
import state.AbstractRoboState;
import state.IdleState;
import strategy.IDrivingStrategy;
import strategy.LineFollowingStrategy;
import strategy.UserControlStrategy;
import strategy.algorithm.ZigZagAlgorithm;
import util.Log;

/**
 * RoboController is the main controller for the robot. It manages the current state of the robot,
 * handles events, and executes the current driving strategy.
 */
public class RoboController implements IEventListener {
  /**
   * The event manager. This is used to dispatch events and register listeners.
   */
  private final EventManager eventManager;
  /**
   * The motor controller. This is used to control the motors of the robot.
   */
  private final IMotorController motorController;
  /**
   * The line following strategy. This is used to follow a line using the light sensor by a given Algorithm (pid or
   * zigzag).
   */
  private final LineFollowingStrategy lineFollowingStrategy;
  /**
   * The user control strategy. This is used to control the robot using user input from the bluetooth connection via
   * move commands.
   */
  private final UserControlStrategy userControlStrategy;
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
  public RoboController(EventManager eventManager, IMotorController motorController) {
    if (eventManager == null || motorController == null) {
      throw new NullPointerException();
    }

    this.eventManager = eventManager;
    this.motorController = motorController;

    this.lineFollowingStrategy = new LineFollowingStrategy(new ZigZagAlgorithm(this));
    this.userControlStrategy = new UserControlStrategy(motorController);

    this.setState(new IdleState());
    this.eventManager.addListener(this);

    Log.info("RoboController initialized. Current state: IdleState");
  }

  /**
   * This method is called to run the current driving strategy. It will execute the strategy and handle any exceptions
   * that occur. So it does not directly interact with the robot hardware but rather uses a strategy pattern.
   */
  public void run() {
    Log.info("RoboController started...");
    IDrivingStrategy strategy = this.currentDrivingStrategy;

    if (strategy == null) {
      return;
    }

    try {
      strategy.execute(this);
    } catch (Exception e) {
      Log.error("Error in RoboController.", e);
    }
  }

  /**
   * This method is called to handle incoming events. So the RoboController acts as a EventListener registered in the
   * EventManager to receive all dispatched events by any class within the system. It will dispatch the event to the
   * current state and handle any exceptions that occur.
   *
   * @param event The event to handle.
   */
  @Override
  public void onEvent(AbstractEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    if (event.getTimestamp() < 0) {
      Log.warning("Timestamp is negative");
      return;
    }

    long currentTime = System.currentTimeMillis();
    long timeDifference = Math.abs(currentTime - event.getTimestamp());

    if (timeDifference > 1000) {
      Log.warning("Ignore old event. Time difference: " + timeDifference + "ms");
      return;
    }

    if (event instanceof SensorEvent) {
      this.handleSensorEvent((SensorEvent)event);
    }

    AbstractRoboState stateToNotify;
    synchronized (this) { stateToNotify = this.currentState; }

    if (stateToNotify != null) {
      stateToNotify.handleEvent(this, event);
    } else {
      Log.warning("No current state to handle event");
    }
  }

  /**
   * This method is called to set the current state of the robot. It will call the onExit method of the current state
   * and the onEnter method of the new state.
   *
   * @param newState The new state to set.
   */
  public synchronized void setState(AbstractRoboState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("State cannot be null");
    }

    if (newState == this.currentState) {
      Log.warning("State is already set the same");
      return;
    }

    if (this.currentState != null) {
      this.currentState.onExit(this);
    }

    this.currentState = newState;
    this.currentState.onEnter(this);
  }

  /**
   * This method is called to set the current driving strategy. It will call the deactivate method of the current and
   * the activate method of the new state.
   */
  public void setCurrentDrivingStrategy(IDrivingStrategy strategy) {
    if (strategy == this.currentDrivingStrategy) {
      Log.warning("Strategy is already set the same");
      return;
    }

    if (this.currentDrivingStrategy != null) {
      this.currentDrivingStrategy.deactivate(this);
    }

    Log.info("Active strategy set to: " + (strategy != null ? strategy.getClass() : "null"));
    this.currentDrivingStrategy = strategy;

    if (strategy != null) {
      strategy.activate(this);
    }
  }

  /**
   * This method is called to handle incoming sensor events. It will update the last light sensor value and the last
   * distance sensor value.
   *
   * @param event The sensor event to handle.
   */
  private void handleSensorEvent(SensorEvent event) {
    if (event.getSensorType() == SensorType.LIGHT) {
      this.lastLightSensorValue = event.getValue();
    }

    if (event.getSensorType() == SensorType.ULTRASONIC) {
      this.lastDistanceSensorValue = event.getValue();
    }
  }

  /**
   * @return The current driving strategy.
   */
  public synchronized AbstractRoboState getCurrentState() { return this.currentState; }

  /**
   * @return The motor controller.
   */
  public IMotorController getMotorController() { return this.motorController; }

  /**
   * @return The event manager.
   */
  public EventManager getEventManager() { return this.eventManager; }

  /**
   * @return The line following strategy.
   */
  public LineFollowingStrategy getLineFollowingStrategy() { return this.lineFollowingStrategy; }

  /**
   * @return The user control strategy.
   */
  public UserControlStrategy getUserControlStrategy() { return this.userControlStrategy; }

  /**
   * @return The last value read from the light sensor.
   */
  public int getLastLightSensorValue() { return this.lastLightSensorValue; }

  /**
   * @return The last value read from the distance sensor.
   */
  public int getLastDistanceSensorValue() { return this.lastDistanceSensorValue; }
}
