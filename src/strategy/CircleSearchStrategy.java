package strategy;

import core.RoboController;
import io.abstracts.IMotorController;
import io.sensor.SensorValueStore;
import main.Config;
import strategy.abstracts.IFindLineStrategy;
import util.Log;

/**
 * CircleSearchAlgorithm is a concrete implementation of the IFollowingAlgorithm interface.
 * This class represents a circle search algorithm used to find a line by moving in circles.
 * It handles the initialization, deinitialization, and execution of the algorithm.
 */
public class CircleSearchStrategy implements IFindLineStrategy {
    /**
     * The maximum search radius for the circle search algorithm.
     */
    private static final int MAX_SEARCH_RADIUS = 5;
    /**
     * The initial duration for the first circle search radius.
     */
    private static final int INITIAL_RADIUS_DURATION_MS = 1000;
    /**
     * The increment in duration for each later circle search radius.
     */
    private static final int RADIUS_INCREMENT_MS = 500;
    /**
     * The default tolerance for light value comparison.
     */
    private static final int DEFAULT_TOLERANCE = 2;

    private static final int waitingPeriodMs = 200;
    /**
     * The RoboController instance used to control the robot.
     */
    private final RoboController controller;
    /**
     * The motor controller used to control the motors of the robot.
     */
    private final IMotorController motorController;
    /**
     * The sensor value store used to access the latest sensor readings.
     */
    private final SensorValueStore sensorValueStore;

    // Interner Zustand
    private int currentRadius = 0;
    private long currentStepStartTime = 0;
    private boolean isWaiting = false;
    private long waitStartTime = 0;

    /**
     * Constructor for the CircleSearchAlgorithm class.
     *
     * @param controller The RoboController instance used to control the robot.
     * @throws NullPointerException if the motor controller or sensor value store is null.
     */
    public CircleSearchStrategy(RoboController controller) {
        if (controller == null || controller.getContext().getMotorController() == null ||
                controller.getContext().getSensorValueStore() == null) {
            throw new NullPointerException();
        }

        this.controller = controller;
        this.motorController = controller.getContext().getMotorController();
        this.sensorValueStore = controller.getContext().getSensorValueStore();
    }

    @Override
    public void initialize() {
        Log.info("CircleSearchStrategy initialized");
        currentRadius = 0;
        currentStepStartTime = System.currentTimeMillis();
        isWaiting = false;
        waitStartTime = 0;
        motorController.stopMotors(true);
    }

    @Override
    public void deinitialize() {
        Log.info("CircleSearchAlgorithm deinitialize");
        motorController.stopMotors(true);
    }

    @Override
    public void run() {
        if (currentRadius >= MAX_SEARCH_RADIUS) {
            Log.info("[MAX REACHED] Line could not be found in Circle Search Strategy.");
            return;
        }

        long now = System.currentTimeMillis();

        if (isWaiting) {
            if (now - waitStartTime >= waitingPeriodMs) {
                isWaiting = false;
                currentRadius++;
                currentStepStartTime = now;
            }
            return;
        }

        int duration = INITIAL_RADIUS_DURATION_MS + currentRadius * RADIUS_INCREMENT_MS;
        if (now - currentStepStartTime < duration) {
            motorController.forward(Config.MOTOR_MIN_SPEED.getIntValue(), Config.MOTOR_MAX_SPEED.getIntValue());
            int lightValue = sensorValueStore.getLastLightSensorValue();

            if (lightValue >= 0 &&
                Math.abs(lightValue - sensorValueStore.getLineEdgeLightValue()) <= DEFAULT_TOLERANCE) {
                Log.info("Line found in Circle No.: " + currentRadius);
            }
        } else {
            motorController.stopMotors(true);
            isWaiting = true;
            waitStartTime = now;
        }
    }
}
