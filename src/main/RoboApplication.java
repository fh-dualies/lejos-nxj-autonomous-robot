package main;

import core.EventLoop;
import core.RoboController;
import event.EventManager;
import io.actuator.NxtMotorController;
import io.sensor.reader.LightSensorReader;
import io.sensor.reader.UltrasonicSensorReader;
import lejos.nxt.SensorPort;

public class RoboApplication {
  public static void main(String[] args) {
    System.out.println("Megamen Robo Application is starting...");

    EventManager eventManager = new EventManager();
    NxtMotorController nxtMotorController = new NxtMotorController();

    RoboController roboController = new RoboController(eventManager, nxtMotorController);

    // TODO: check ports
    LightSensorReader lightSensorReader = new LightSensorReader(SensorPort.S1, eventManager);
    UltrasonicSensorReader ultrasonicSensorReader = new UltrasonicSensorReader(SensorPort.S2, eventManager);

    final EventLoop eventLoop = new EventLoop(roboController, lightSensorReader, ultrasonicSensorReader);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Shutting down megamen...");
        eventLoop.stop();

        try {
          // wait for the event loop to finish
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });

    eventLoop.run();
    System.out.println("Megamen out.");
  }
}