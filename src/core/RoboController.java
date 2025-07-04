package core;

import domain.command.base.ICommand;
import domain.command.impl.ExitCommand;
import domain.command.impl.OrientationCommand;
import domain.event.EventManager;
import domain.event.base.AbstractEvent;
import domain.event.base.IEventListener;
import domain.event.base.IExposableEvent;
import domain.event.impl.ButtonEvent;
import domain.event.impl.ChangeStateEvent;
import domain.event.impl.CommandEvent;
import domain.event.impl.SensorEvent;
import domain.state.base.AbstractRoboState;
import domain.state.impl.CalibrationState;
import domain.strategy.base.IDrivingStrategy;
import io.actuator.base.IMotorController;
import io.connection.impl.BluetoothTransmitter;
import io.sensor.SensorValueStore;
import lejos.nxt.Button;
import shared.constants.OrientationEnum;
import shared.util.Logger;

/**
 * RoboController is the main controller for the robot. It handles events and executes
 * the robot's driving strategy, using RoboContext to store and access state.
 */
public final class RoboController implements IEventListener {
  /**
   * The context containing all state data for the robot.
   */
  private final RoboContext context;

  /**
   * Constructor for the RoboController class.
   *
   * @param eventManager    The event manager used to dispatch events and register listeners.
   * @param motorController The motor controller used to control the motors of the robot.
   */
  public RoboController(EventManager eventManager, IMotorController motorController,
                        BluetoothTransmitter bluetoothTransmitter) {
    this.context =
        new RoboContext(eventManager, motorController, bluetoothTransmitter, new SensorValueStore(eventManager));

    this.setState(new CalibrationState());
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
      Logger.error("RoboController error.", e);
    }
  }

  /**
   * This method is called to check for pressed buttons. It checks if the ENTER or ESCAPE button
   * is pressed and dispatches the corresponding event.
   */
  public void checkForPressedButtons() {
    EventManager eventManager = this.context.getEventManager();

    if (Button.ENTER.isDown()) {
      eventManager.dispatch(new ButtonEvent(Button.ENTER.toString()));
    }

    if (Button.ESCAPE.isDown()) {
      eventManager.dispatch(new ButtonEvent(Button.ESCAPE.toString()));
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
      Logger.warning("event timestamp invalid");
      return;
    }

    long currentTime = System.currentTimeMillis();
    long timeDifference = Math.abs(currentTime - event.getTimestamp());

    if (timeDifference > 1000) {
      Logger.warning("ignore old event");
      return;
    }

    if (event instanceof ButtonEvent) {
      this.handleButtonEvent((ButtonEvent)event);
    }

    if (event instanceof CommandEvent) {
      this.handleCommandEvent((CommandEvent)event);
    }

    if (event instanceof SensorEvent) {
      this.context.getSensorValueStore().updateFromSensorEvent((SensorEvent)event);
    }

    if (event instanceof IExposableEvent) {
      this.context.getBluetoothTransmitter().addExposableEvent((IExposableEvent)event);
    }

    AbstractRoboState stateToNotify = this.context.getCurrentState();

    if (stateToNotify != null) {
      stateToNotify.handleEvent(this, event);
    } else {
      Logger.warning("no state to notify");
    }
  }

  private void handleButtonEvent(ButtonEvent event) {
    if (event == null) {
      return;
    }

    String buttonId = event.getButtonId();

    if (buttonId == null || buttonId.isEmpty()) {
      return;
    }

    if (buttonId.equals(Button.ESCAPE.toString())) {
      System.exit(0);
    }
  }

  /**
   * This method is called to handle command events.
   *
   * @param event The command event to handle.
   */
  private void handleCommandEvent(CommandEvent event) {
    if (event == null) {
      return;
    }

    ICommand command = event.getCommand();

    if (command instanceof OrientationCommand) {
      this.handleOrientationCommand((OrientationCommand)command);
    }

    if (command instanceof ExitCommand) {
      ((ExitCommand)command).execute();
    }
  }

  /**
   * This method is called to handle orientation commands. It sets the orientation of the robot
   * if it is different from the current orientation.
   *
   * @param command The orientation command to handle.
   */
  private void handleOrientationCommand(OrientationCommand command) {
    if (command == null) {
      return;
    }

    OrientationEnum orientation = command.getOrientation();

    if (orientation == null) {
      return;
    }

    if (this.context.getOrientation() == orientation) {
      Logger.warning("orientation already set");
      return;
    }

    this.context.setOrientation(orientation);
    Logger.info("orientation set to: " + orientation);
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
      Logger.warning("state already set");
      return;
    }

    if (currentState != null) {
      currentState.onExit(this);
    }

    this.context.setCurrentState(newState);
    newState.onEnter(this);

    this.getContext().getEventManager().dispatch(new ChangeStateEvent(newState.getState()));
  }

  /**
   * This method is called to set the current driving strategy. It will call the deactivate method of the current and
   * the activate method of the new state.
   */
  public void setCurrentDrivingStrategy(IDrivingStrategy strategy) {
    IDrivingStrategy currentStrategy = this.context.getCurrentDrivingStrategy();

    if (strategy == currentStrategy) {
      Logger.warning("strategy already set");
      return;
    }

    if (currentStrategy != null) {
      currentStrategy.deactivate(this);
    }

    Logger.info("new strategy: " + (strategy != null ? strategy.getClass() : "null"));
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