package strategy.algorithm;

import io.sensor.DefaultSensorValues;

import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class ZigZagAlgorithm implements IFollowingAlgorithm {
    private NXTRegulatedMotor left;
    private NXTRegulatedMotor right;
    private LightSensor lightSensor;
    private UltrasonicSensor distanceSensor;

    public ZigZagAlgorithm(NXTRegulatedMotor motorLeft, NXTRegulatedMotor motorRight,
                           LightSensor lightSensor, UltrasonicSensor distanceSensor) {
        this.left = motorLeft;
        this.right = motorRight;
        this.lightSensor = lightSensor;
        this.distanceSensor = distanceSensor;
    }

    @override
    public void initialize() {
        left.setSpeed(MOTOR_MEDIUM_SPEED);
        right.setSpeed(MOTOR_MEDIUM_SPEED);
    }

    @Override
    public void go() {
        while (!Button.ESCAPE.isDown()) {
            if (distanceSensor.getDistance() < DISTANCE_STOP_THRESHOLD) {
                // Obstacle detected, stop the motors
                left.stop();
                right.stop();
                break;
            }
            if (lightSensor.getLightValue() < LIGHT_FLOOR_MIN) {
                // On line -> "Right" Zig
                left.forward();
                right.backward();
                Delay.msDelay(ZIGZAG_TIME);

                left.forward();
                right.forward();
                Delay.msDelay(ZIGZAG_TIME);
            } else {
                // Off line -> "Left" Zack
                left.backward();
                right.forward();
                Delay.msDelay(ZIGZAG_TIME);

                left.forward();
                right.forward();
                Delay.msDelay(ZIGZAG_TIME);
            }
        }
    }
}
