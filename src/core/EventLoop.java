package core;

import app.Config;
import io.connection.impl.BluetoothReceiver;
import io.connection.impl.BluetoothTransmitter;
import io.sensor.impl.LightSensorReader;
import io.sensor.impl.UltrasonicSensorReader;
import lejos.util.Delay;
import shared.util.Logger;
import shared.util.SystemMonitor;

/**
 * The main event loop for the robot.
 * This class is responsible for managing the Bluetooth connection,
 * reading sensor values, and controlling the motors. The event loop
 * runs continuously until stopped, processing commands and sensor data
 * with a fixed delay between iterations. It also handles resource
 * cleanup when the loop is terminated.
 */
public final class EventLoop implements Runnable {
  /**
   * The delay in milliseconds between iterations of the event loop. This controls the frequency of
   * sensor checks and command processing.
   */
  private static final int LOOP_DELAY = Config.LOOP_DELAY.getIntValue();

  /**
   * The RoboController instance that manages the robot's behavior. It serves as a context object,
   * providing access to sensor data and motor control functionalities throughout the application.
   */
  private final RoboController controller;

  /**
   * The BluetoothReceiver instance that manages the Bluetooth connection and command reception.
   * It is responsible for establishing a connection with the remote device and processing incoming
   * commands.
   */
  private final BluetoothReceiver bluetoothReceiver;

  /**
   * The BluetoothTransmitter instance that manages the Bluetooth transmission of data. It is
   * responsible for sending data to the remote device over the established Bluetooth connection.
   */
  private final BluetoothTransmitter bluetoothTransmitter;

  /**
   * The LightSensorReader instance that reads values from the light sensor. It is responsible for
   * checking the light sensor's value and providing it to the RoboController for processing.
   */
  private final LightSensorReader lightSensorReader;

  /**
   * The UltrasonicSensorReader instance that reads values from the ultrasonic sensor. It is
   * responsible for checking the ultrasonic sensor's value and providing it to the RoboController
   * for processing.
   */
  private final UltrasonicSensorReader ultrasonicSensorReader;

  /**
   * A flag indicating whether the event loop is currently running. This is used to control the
   * execution of the loop and to stop it gracefully when needed.
   */
  private volatile boolean running = false;

  /**
   * Constructor for the EventLoop class.
   *
   * @param controller             The RoboController instance that manages the robot's behavior.
   * @param lightSensorReader      The LightSensorReader instance for reading light sensor values.
   * @param ultrasonicSensorReader The UltrasonicSensorReader instance for reading ultrasonic sensor values.
   * @param bluetoothReceiver      The BluetoothReceiver instance for managing Bluetooth connections and commands.
   */
  public EventLoop(RoboController controller, LightSensorReader lightSensorReader,
                   UltrasonicSensorReader ultrasonicSensorReader, BluetoothReceiver bluetoothReceiver,
                   BluetoothTransmitter bluetoothTransmitter) {
    if (controller == null || lightSensorReader == null || ultrasonicSensorReader == null ||
        bluetoothReceiver == null) {
      throw new NullPointerException();
    }

    this.controller = controller;
    this.lightSensorReader = lightSensorReader;
    this.ultrasonicSensorReader = ultrasonicSensorReader;
    this.bluetoothReceiver = bluetoothReceiver;
    this.bluetoothTransmitter = bluetoothTransmitter;
  }

  @Override
  public void run() {
    Logger.info("start loop");

    if (!this.bluetoothReceiver.waitForConnection()) {
      Logger.error("BT failed");
      return;
    }

    this.bluetoothTransmitter.setupConnection(this.bluetoothReceiver.getConnection());

    Logger.info("BT connected");
    this.running = true;

    while (this.running) {
      try {
        this.lightSensorReader.checkValue();
        this.ultrasonicSensorReader.checkValue();

        this.bluetoothReceiver.checkForCommands();
        this.bluetoothTransmitter.exposeEvents();

        this.controller.checkForPressedButtons();
        this.controller.run();

        SystemMonitor.logMemoryUsage();
        Delay.msDelay(LOOP_DELAY);
      } catch (Exception e) {
        Logger.error("loop error", e);
      }
    }

    Logger.info("loop stopped");
    this.cleanup();
    Logger.info("loop cleaned");
  }

  /**
   * Stops the event loop by setting the running flag to false. This will cause the loop to exit gracefully
   */
  public void stop() { this.running = false; }

  /**
   * Cleans up resources used by the event loop. This includes closing the Bluetooth connection,
   * stopping the motor controller, and closing the light sensor reader.
   */
  private void cleanup() {
    try {
      this.bluetoothReceiver.closeConnection();
      this.bluetoothTransmitter.closeConnection();
      this.controller.getContext().getMotorController().close();
      lightSensorReader.close();
    } catch (Exception e) {
      Logger.error("cleanup error", e);
    }
  }
}
