package app;

import core.EventLoop;
import core.RoboController;
import domain.event.EventManager;
import io.actuator.impl.NxtMotorController;
import io.connection.impl.BluetoothReceiver;
import io.connection.impl.BluetoothTransmitter;
import io.sensor.impl.LightSensorReader;
import io.sensor.impl.UltrasonicSensorReader;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import shared.util.Logger;

/**
 * Main class for the Megamen Robo Application.
 * This class initializes the application, sets up the event loop, and handles shutdown procedures.
 */
public final class RoboApplication {
  public static final boolean DEBUG = true;

  /**
   * Sets up the event loop and initializes the necessary parts for the robot.
   *
   * @return The initialized EventLoop instance.
   */
  private static EventLoop setupEventLoop() {
    EventManager eventManager = new EventManager();
    Logger.setEventManager(eventManager);
    NxtMotorController nxtMotorController = new NxtMotorController();
    BluetoothTransmitter bluetoothTransmitter = new BluetoothTransmitter();

    RoboController roboController = new RoboController(eventManager, nxtMotorController, bluetoothTransmitter);

    LightSensorReader lightSensorReader = new LightSensorReader(SensorPort.S1, eventManager);
    UltrasonicSensorReader ultrasonicSensorReader = new UltrasonicSensorReader(SensorPort.S4, eventManager);

    BluetoothReceiver bluetoothReceiver = new BluetoothReceiver(eventManager);

    return new EventLoop(roboController, lightSensorReader, ultrasonicSensorReader, bluetoothReceiver,
                         bluetoothTransmitter);
  }

  /**
   * Main method to start the Megamen Robo Application.
   *
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args) {
    Logger.info("starting megamen");

    final EventLoop eventLoop = setupEventLoop();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        Logger.info("shutting down megamen");
        eventLoop.stop();

        // wait for the event loop to finish
        Delay.msDelay(100);
      }
    });

    eventLoop.run();

    Logger.info("megamen out.");
  }
}