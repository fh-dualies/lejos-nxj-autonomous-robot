import lejos.nxt.*;
import lejos.robotics.objectdetection.*;

public class Main {
	public static void main(String[] args) {
		Motor.A.setSpeed(720);
		Motor.B.setSpeed(720);
	
		LightSensor ls = new LightSensor(SensorPort.S1);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
		FeatureDetector fd = new RangeFeatureDetector(us, 50, 500);
		
		Motor.A.forward();
		Motor.B.forward();
		
		while (fd.scan() == null) {
			LCD.drawInt(ls.getLightValue(), 4, 0, 0);
			LCD.drawInt(ls.getNormalizedLightValue(), 4, 0, 2);
			
			if (ls.getNormalizedLightValue() <= 450) {
				break;
			}
		}	
		
		Motor.A.stop();
		Motor.B.stop();
		
		System.out.println("detected");
	
		Button.waitForAnyPress();
	}
}