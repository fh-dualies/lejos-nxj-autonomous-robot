package core;

import io.sensor.reader.LightSensorReader;
import io.sensor.reader.UltrasonicSensorReader;
import java.util.Objects;

public class EventLoop implements Runnable {
  private final RoboController controller;
  private final LightSensorReader lightSensorReader;
  private final UltrasonicSensorReader ultrasonicSensorReader;

  private static final int LOOP_DELAY = 20;
  private volatile boolean running = false;

  public EventLoop(RoboController controller, LightSensorReader lightSensorReader,
                   UltrasonicSensorReader ultrasonicSensorReader) {
    this.controller = Objects.requireNonNull(controller);
    this.lightSensorReader = Objects.requireNonNull(lightSensorReader);
    this.ultrasonicSensorReader = Objects.requireNonNull(ultrasonicSensorReader);

    // TODO: add bt connection
  }

  @Override
  public void run() {
    System.out.println("EventLoop started");
    this.running = true;

    while (running) {
      try {
        lightSensorReader.checkValue();
        ultrasonicSensorReader.checkValue();

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
      // TODO: add bt connection cleanup

      this.controller.getMotorController().close();
      lightSensorReader.close();
    } catch (Exception e) {
      System.err.println("Error during cleanup: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
