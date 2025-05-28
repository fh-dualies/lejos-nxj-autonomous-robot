package strategy;

import core.RoboController;
import event.ButtonEvent;
import event.CommandEvent;
import event.SensorEvent;
import event.abstracts.AbstractEvent;
import event.abstracts.IEventListener;
import io.command.CalibrationCommand;
import io.constants.CalibrationStepEnum;
import io.constants.SensorTypeEnum;
import io.sensor.SensorValueStore;
import lejos.nxt.Button;
import state.IdleState;
import strategy.abstracts.IDrivingStrategy;
import util.LcdUtil;
import util.Logger;

/**
 * CalibrationStrategy is a concrete implementation of the IDrivingStrategy interface.
 * This class represents the calibration strategy of the robot, where it calibrates its sensors.
 * It handles entering and exiting the strategy, as well as processing events that occur while in this strategy.
 */
public class CalibrationStrategy implements IDrivingStrategy, IEventListener {
  /**
   * The delay in milliseconds to debounce button events.
   */
  private static final int DEBOUNCE_DELAY = 1000;
  /**
   * The sensor value store used to store the light sensor values.
   */
  private final SensorValueStore sensorValueStore;
  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;
  /**
   * The last time a button event was processed.
   * This is used to prevent multiple button events from being processed too quickly.
   */
  private long lastButtonEventProcessedTime = 0;
  /**
   * The light values for the floor and stripe.
   */
  private int floorLightValue = -1;
  private int stripeLightValue = -1;

  /**
   * The current calibration step.
   */
  private CalibrationStepEnum step = CalibrationStepEnum.FLOOR;

  /**
   * Constructor for the CalibrationStrategy class.
   *
   * @param controller The RoboController instance used to control the robot.
   * @throws NullPointerException if the motor controller is null.
   */
  public CalibrationStrategy(RoboController controller) {
    if (controller == null || controller.getContext().getSensorValueStore() == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.sensorValueStore = controller.getContext().getSensorValueStore();
  }

  /**
   * Executes the calibration strategy.
   */
  @Override
  public void execute(RoboController controller) {
    LcdUtil.clear();
    LcdUtil.print("Calibrating", LcdUtil.Position.INFO);

    switch (step) {
    case FLOOR:
      LcdUtil.print("Floor light", LcdUtil.Position.INFO);
      break;
    case STRIPE:
      LcdUtil.print("Floor: " + floorLightValue, LcdUtil.Position.INFO);
      LcdUtil.print("Stripe light", LcdUtil.Position.ERROR);
      break;
    case DONE:
      LcdUtil.print("Floor: " + floorLightValue, LcdUtil.Position.INFO);
      LcdUtil.print("Stripe: " + stripeLightValue, LcdUtil.Position.ERROR);
      break;
    default:
      throw new IllegalStateException("Unexpected value: " + step);
    }
  }

  /**
   * Activates the calibration strategy.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  @Override
  public void activate(RoboController controller) {
    Logger.info("CalibrationStrategy activated");

    controller.getContext().getEventManager().addListener(this);
  }

  /**
   * Deactivates the calibration strategy.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  @Override
  public void deactivate(RoboController controller) {
    Logger.info("CalibrationStrategy deactivated");

    controller.getContext().getEventManager().removeListener(this);
  }

  /**
   * Handles events that occur while in the calibration strategy.
   * This method processes the event and calls the appropriate methods to handle it.
   * It also handles state switching events by calling the handleSwitchEvent method.
   * Will be called by the RoboController after an event is received.
   *
   * @param event The event that occurred.
   */
  @Override
  public void onEvent(AbstractEvent event) {
    if (event instanceof SensorEvent) {
      this.handleSensorEvent((SensorEvent)event);
    }

    if (event instanceof ButtonEvent) {
      this.handleButtonEvent((ButtonEvent)event);
    }

    if (event instanceof CommandEvent) {
      this.handleCommandEvent((CommandEvent)event);
    }
  }

  /**
   * Handles sensor events.
   * This method checks if the sensor type is LIGHT and prints the sensor value to the LCD.
   *
   * @param sensorEvent The sensor event to handle.
   */
  private void handleSensorEvent(SensorEvent sensorEvent) {
    if (!sensorEvent.getSensorType().equals(SensorTypeEnum.LIGHT)) {
      return;
    }

    LcdUtil.print("#" + sensorEvent.getValue(), LcdUtil.Position.STREAM);
  }

  /**
   * Handles button events.
   * This method checks if the button pressed is the ENTER button and calls handleStateChange to process the event.
   *
   * @param event The button event to handle.
   */
  private void handleButtonEvent(ButtonEvent event) {
    if (!event.getButtonId().equals(Button.ENTER.toString())) {
      return;
    }

    this.handleStateChange();
  }

  /**
   * Handles command events.
   * This method checks if the command is a CalibrationCommand and if the step matches the current step.
   * If so, it calls handleStateChange to process the command.
   *
   * @param event The command event to handle.
   */
  private void handleCommandEvent(CommandEvent event) {
    if (!(event.getCommand() instanceof CalibrationCommand)) {
      return;
    }

    CalibrationStepEnum commandStep = ((CalibrationCommand)event.getCommand()).getStep();

    if (commandStep == null) {
      return;
    }

    if (!commandStep.equals(this.step)) {
      Logger.warning("Received command for step " + commandStep + ", but current step is " + this.step);
      return;
    }

    this.handleStateChange();
  }

  /**
   * Handles the state change based on the current step.
   * This method updates the light values for the floor and stripe based on the sensor readings.
   * It also updates the calibration values in the sensor value store and transitions to the next step.
   */
  private void handleStateChange() {
    long now = System.currentTimeMillis();

    if (now - this.lastButtonEventProcessedTime < DEBOUNCE_DELAY) {
      return;
    }

    this.lastButtonEventProcessedTime = now;
    int currentValue = this.sensorValueStore.getLastLightSensorValue();

    switch (step) {
    case FLOOR:
      this.floorLightValue = currentValue;
      this.step = CalibrationStepEnum.STRIPE;
      break;
    case STRIPE:
      this.stripeLightValue = currentValue;
      this.step = CalibrationStepEnum.DONE;
      break;
    case DONE:
      this.sensorValueStore.updateCalibrationValues(this.floorLightValue, this.stripeLightValue);
      this.controller.setState(new IdleState());
      break;
    default:
      throw new IllegalStateException("Unexpected value: " + step);
    }
  }
}
