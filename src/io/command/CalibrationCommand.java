package io.command;

import io.constants.CalibrationStepEnum;

/**
 * CalibrationCommand represents a command to calibrate the robot's sensors.
 * It is used to initiate the calibration process, which may involve reading sensor values
 * and adjusting the robot's state based on those values.
 */
public class CalibrationCommand implements ICommand {
  /**
   * The calibration step associated with this command.
   */
  private final CalibrationStepEnum step;

  /**
   * Constructor for the CalibrationCommand class.
   *
   * @param step The calibration step to be executed.
   * @throws NullPointerException if a step is null.
   */
  public CalibrationCommand(CalibrationStepEnum step) {
    if (step == null) {
      throw new NullPointerException();
    }

    this.step = step;
  }

  /**
   * Returns the calibration step associated with this command.
   *
   * @return The calibration step.
   */
  public CalibrationStepEnum getStep() { return this.step; }
}
