package strategy;

/**
 * CalibrationStepEnum represents the different steps in the calibration process of the robot.
 * Each step corresponds to a specific action that needs to be performed during calibration.
 */
public enum CalibrationStepEnum {
  FLOOR,  // Step for calibrating the floor light sensor
  STRIPE, // Step for calibrating the stripe light sensor
  DONE    // Final step indicating calibration is complete
}
