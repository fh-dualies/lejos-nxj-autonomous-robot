package core;

import io.connection.BluetoothReceiver;
import io.sensor.reader.LightSensorReader;
import io.sensor.reader.UltrasonicSensorReader;
import java.util.Objects;

public class EventLoop implements Runnable {
  private final RoboController controller;
  private final BluetoothReceiver bluetoothReceiver;
  private final LightSensorReader lightSensorReader;
  private final UltrasonicSensorReader ultrasonicSensorReader;

  private static final int LOOP_DELAY = 20;
  private volatile boolean running = false;

  public EventLoop(RoboController controller, LightSensorReader lightSensorReader,
                   UltrasonicSensorReader ultrasonicSensorReader, BluetoothReceiver bluetoothReceiver) {
    this.controller = Objects.requireNonNull(controller);
    this.lightSensorReader = Objects.requireNonNull(lightSensorReader);
    this.ultrasonicSensorReader = Objects.requireNonNull(ultrasonicSensorReader);
    this.bluetoothReceiver = Objects.requireNonNull(bluetoothReceiver);
  }

  @Override
  public void run() {
    System.out.println("EventLoop started...");

    if (!this.bluetoothReceiver.waitForConnection()) {
      System.err.println("Failed to establish Bluetooth connection");
      return;
    }

    this.running = true;

    while (running) {
      try {
        this.lightSensorReader.checkValue();
        this.ultrasonicSensorReader.checkValue();

        this.bluetoothReceiver.checkForCommands();
        this.controller.run();

        // TODO: maybe use Delay class of lejos instead?
        Thread.sleep(LOOP_DELAY);
      } catch (InterruptedException e) {
        this.running = false;
        System.out.println("EventLoop interrupted");
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        System.err.println("Error in EventLoop: " + e.getMessage());
        e.printStackTrace();
      }
    }

    System.out.println("EventLoop stopped");
    this.cleanup();
    System.out.println("EventLoop cleanup done");
  }

  public void stop() { this.running = false; }

  private void cleanup() {
    try {
      this.bluetoothReceiver.closeConnection();
      this.controller.getMotorController().close();
      lightSensorReader.close();
    } catch (Exception e) {
      System.err.println("Error during cleanup: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
