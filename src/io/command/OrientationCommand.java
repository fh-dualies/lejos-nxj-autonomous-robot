package io.command;

import io.abstracts.ICommand;
import io.constants.OrientationEnum;

/**
 * OrientationCommand represents a command to set the robot's orientation.
 * It is used to specify the direction in which the robot should face.
 */
public class OrientationCommand implements ICommand {
  /**
   * The orientation associated with this command.
   */
  private final OrientationEnum orientation;

  /**
   * Constructor for the OrientationCommand class.
   *
   * @param orientation The orientation to be set for the robot.
   * @throws NullPointerException if the orientation is null.
   */
  public OrientationCommand(OrientationEnum orientation) {
    if (orientation == null) {
      throw new NullPointerException("Orientation cannot be null");
    }

    this.orientation = orientation;
  }

  /**
   * Returns the orientation associated with this command.
   *
   * @return The orientation to be set for the robot.
   */
  public OrientationEnum getOrientation() { return this.orientation; }
}
