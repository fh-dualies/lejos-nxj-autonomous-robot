package core;

import event.EventManager;
import event.IEventListener;
import event.base.AbstractEvent;
import event.base.SensorEvent;
import io.actuator.IMotorController;
import io.sensor.SensorType;
import java.util.Objects;
import state.AbstractRoboState;
import state.IdleState;
import strategy.IDrivingStrategy;
import strategy.LineFollowingStrategy;
import strategy.UserControlStrategy;

public class RoboController implements IEventListener {
  private AbstractRoboState currentState = null;
  private IDrivingStrategy currentDrivingStrategy = null;
  private final EventManager eventManager;
  private final IMotorController motorController;

  // pre initialized strategies (to avoid null checks)
  private final LineFollowingStrategy lineFollowingStrategy;
  private final UserControlStrategy userControlStrategy;

  // sensors
  private int lastLightSensorValue = -1;
  private int lastDistanceSensorValue = -1;

  public RoboController(EventManager eventManager, IMotorController motorController) {
    this.eventManager = Objects.requireNonNull(eventManager);
    this.motorController = Objects.requireNonNull(motorController);

    this.lineFollowingStrategy = new LineFollowingStrategy(motorController);
    this.userControlStrategy = new UserControlStrategy(motorController);

    this.setState(new IdleState());
    this.eventManager.addListener(this);
  }

  public void run() {
    IDrivingStrategy strategy = this.currentDrivingStrategy;

    if (strategy == null) {
      return;
    }

    try {
      strategy.execute(this);
    } catch (Exception e) {
      System.err.println("RoboController error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void onEvent(AbstractEvent event) {
    if (event instanceof SensorEvent) {
      this.handleSensorEvent((SensorEvent)event);
    }

    AbstractRoboState stateToNotify;
    synchronized (this) { stateToNotify = this.currentState; }

    if (stateToNotify != null) {
      stateToNotify.handleEvent(this, event);
    }
  }

  public synchronized void setState(AbstractRoboState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("State cannot be null");
    }

    if (this.currentState != null) {
      this.currentState.onExit(this);
    }

    this.currentState = newState;
    this.currentState.onEnter(this);
  }

  private void handleSensorEvent(SensorEvent event) {
    if (event.getSensorType() == SensorType.LIGHT) {
      this.lastLightSensorValue = event.getValue();
    }

    if (event.getSensorType() == SensorType.ULTRASONIC) {
      this.lastDistanceSensorValue = event.getValue();
    }
  }

  public synchronized AbstractRoboState getCurrentState() { return this.currentState; }

  public IMotorController getMotorController() { return this.motorController; }

  public EventManager getEventManager() { return this.eventManager; }

  public LineFollowingStrategy getLineFollowingStrategy() { return this.lineFollowingStrategy; }

  public UserControlStrategy getUserControlStrategy() { return this.userControlStrategy; }

  public int getLastLightSensorValue() { return this.lastLightSensorValue; }

  public int getLastDistanceSensorValue() { return this.lastDistanceSensorValue; }

  public void setCurrentDrivingStrategy(IDrivingStrategy strategy) {
    if (this.currentDrivingStrategy != null) {
      this.currentDrivingStrategy.deactivate(this);
    }

    System.out.println("Active strategy set to: " + (strategy != null ? strategy.getClass().getSimpleName() : "null"));
    this.currentDrivingStrategy = strategy;

    if (strategy != null) {
      strategy.activate(this);
    }
  }
}
