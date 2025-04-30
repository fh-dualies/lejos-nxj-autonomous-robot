import core.EventLoop;
import core.RoboController;
import event.EventManager;
import io.actuator.NxtMotorController;
import io.connection.BluetoothReceiver;
import io.connection.BluetoothTransmitter;
import io.sensor.reader.LightSensorReader;
import io.sensor.reader.UltrasonicSensorReader;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import util.Log;

/**
 * Main class for the Megamen Robo Application.
 * This class initializes the application, sets up the event loop, and handles shutdown procedures.
 */
public class RoboApplication {
  /**
   * Sets up the event loop and initializes the necessary parts for the robot.
   *
   * @return The initialized EventLoop instance.
   */
  private static EventLoop setupEventLoop() {
    EventManager eventManager = new EventManager();
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
   * @param args Command line arguments (not used).
   */
  public static void main(String[] args) {
    Log.info("starting megamen");

    final EventLoop eventLoop = setupEventLoop();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        Log.info("shutting down megamen");
        eventLoop.stop();

        // wait for the event loop to finish
        Delay.msDelay(100);
      }
    });

    eventLoop.run();

    Log.info("megamen out.");
  }
}