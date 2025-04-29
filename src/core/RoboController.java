package core;

import event.EventManager;
import event.IEventListener;
import event.base.AbstractEvent;
import event.base.SensorEvent;
import io.actuator.IMotorController;
import state.AbstractRoboState;
import state.AutonomousState;
import strategy.IDrivingStrategy;
import util.Log;

/**
 * RoboController is the main controller for the robot. It handles events and executes
 * the robot's driving strategy, using RoboContext to store and access state.
 */
public class RoboController implements IEventListener {
  /**
   * The context containing all state data for the robot.
   */
  private final RoboContext context;

  /**
   * @param eventManager    The event manager used to dispatch events and register listeners.
   * @param motorController The motor controller used to control the motors of the robot.
   */
  public RoboController(EventManager eventManager, IMotorController motorController) {
    this.context = new RoboContext(eventManager, motorController);

    this.setState(new AutonomousState());
    this.context.getEventManager().addListener(this);
  }

  /**
   * This method is called to run the current driving strategy. It will execute the strategy
   * and handle any exceptions that occur.
   */
  public void run() {
    IDrivingStrategy strategy = this.context.getCurrentDrivingStrategy();

    if (strategy == null) {
      return;
    }

    try {
      strategy.execute(this);
    } catch (Exception e) {
      Log.error("RoboController error.", e);
    }
  }

  /**
   * This method is called to handle incoming events. It dispatches the event to the
   * current state and handles any exceptions that occur.
   *
   * @param event The event to handle.
   */
  @Override
  public void onEvent(AbstractEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    if (event.getTimestamp() < 0) {
      Log.warning("event timestamp invalid");
      return;
    }

    long currentTime = System.currentTimeMillis();
    long timeDifference = Math.abs(currentTime - event.getTimestamp());

    if (timeDifference > 1000) {
      Log.warning("ignore old event");
      return;
    }

    if (event instanceof SensorEvent) {
      this.context.updateFromSensorEvent((SensorEvent)event);
    }

    AbstractRoboState stateToNotify = this.context.getCurrentState();

    if (stateToNotify != null) {
      stateToNotify.handleEvent(this, event);
    } else {
      Log.warning("no state to notify");
    }
  }

  /**
   * This method is called to set the current state of the robot. It will call the onExit method of the current state
   * and the onEnter method of the new state.
   *
   * @param newState The new state to set.
   */
  public void setState(AbstractRoboState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("State cannot be null");
    }

    AbstractRoboState currentState = this.context.getCurrentState();

    if (newState == currentState) {
      Log.warning("state already set");
      return;
    }

    if (currentState != null) {
      currentState.onExit(this);
    }

    this.context.setCurrentState(newState);
    newState.onEnter(this);
  }

  /**
   * This method is called to set the current driving strategy. It will call the deactivate method of the current and
   * the activate method of the new state.
   */
  public void setCurrentDrivingStrategy(IDrivingStrategy strategy) {
    IDrivingStrategy currentStrategy = this.context.getCurrentDrivingStrategy();

    if (strategy == currentStrategy) {
      Log.warning("strategy already set");
      return;
    }

    if (currentStrategy != null) {
      currentStrategy.deactivate(this);
    }

    Log.info("new strategy: " + (strategy != null ? strategy.getClass() : "null"));
    this.context.setCurrentDrivingStrategy(strategy);

    if (strategy != null) {
      strategy.activate(this);
    }
  }

  /**
   * This method is called to get the current driving strategy.
   *
   * @return The current driving strategy.
   */
  public RoboContext getContext() { return this.context; }
}