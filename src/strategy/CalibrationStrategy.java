package strategy;

import core.RoboController;
import event.ButtonEvent;
import event.SensorEvent;
import event.base.AbstractEvent;
import event.base.IEventListener;
import io.sensor.SensorType;
import io.sensor.SensorValueStore;
import lejos.nxt.Button;
import state.IdleState;
import util.LcdUtil;
import util.Log;

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
   * The last time a button event was processed.
   * This is used to prevent multiple button events from being processed too quickly.
   */
  private long lastButtonEventProcessedTime = 0;

  /**
   * The sensor value store used to store the light sensor values.
   */
  private final SensorValueStore sensorValueStore;

  /**
   * The RoboController instance used to control the robot.
   */
  private final RoboController controller;

  /**
   * The light values for the floor and stripe.
   */
  private int floorLightValue = -1;
  private int stripeLightValue = -1;

  /**
   * The current calibration step.
   */
  private CalibrationStep step = CalibrationStep.FLOOR;

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
    Log.info("CalibrationStrategy activated");

    controller.getContext().getEventManager().addListener(this);
  }

  /**
   * Deactivates the calibration strategy.
   *
   * @param controller The RoboController instance that manages the robot's operations.
   */
  @Override
  public void deactivate(RoboController controller) {
    Log.info("CalibrationStrategy deactivated");

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
  }

  /**
   * Handles sensor events.
   *
   * @param sensorEvent The sensor event to handle.
   */
  private void handleSensorEvent(SensorEvent sensorEvent) {
    if (!sensorEvent.getSensorType().equals(SensorType.LIGHT)) {
      return;
    }

    LcdUtil.print("#" + sensorEvent.getValue(), LcdUtil.Position.STREAM);
  }

  /**
   * Handles button events.
   *
   * @param event The button event to handle.
   */
  private void handleButtonEvent(ButtonEvent event) {
    if (!event.getButtonId().equals(Button.ENTER.toString())) {
      return;
    }

    long now = System.currentTimeMillis();

    if (now - this.lastButtonEventProcessedTime < DEBOUNCE_DELAY) {
      return;
    }

    this.lastButtonEventProcessedTime = now;
    int currentValue = this.sensorValueStore.getLastLightSensorValue();

    switch (step) {
    case FLOOR:
      this.floorLightValue = currentValue;
      this.step = CalibrationStep.STRIPE;
      break;
    case STRIPE:
      this.stripeLightValue = currentValue;
      this.step = CalibrationStep.DONE;
      break;
    case DONE:
      this.sensorValueStore.updateCalibrationValues(this.floorLightValue, this.stripeLightValue);
      this.controller.setState(new IdleState());
      break;
    default:
      throw new IllegalStateException("Unexpected value: " + step);
    }
  }

  /**
   * The possible calibration steps.
   */
  private enum CalibrationStep { FLOOR, STRIPE, DONE }
}
