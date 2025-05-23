package io.command;

/**
 * MoveCommand represents a command to move the robot in a specific direction.
 * It encapsulates the direction of movement and provides a method to retrieve it.
 */
public class MoveCommand implements ICommand {
  /**
   * The direction in which the robot should move.
   */
  private final int speed;
  private final int turnAngle;

  /**
   * Constructor for the MoveCommand class.
   *
   * @throws IllegalArgumentException if speed is lower then zero
   */
  public MoveCommand(int speed, int turnAngle) {
    if (speed < 0) {
      throw new IllegalArgumentException();
    }

    this.speed = speed;
    this.turnAngle = turnAngle;
  }

  /**
   * Sets the speed of the robot.
   *
   * @return the speed at which the robot should move
   */
  public int getSpeed() { return this.speed; }

  /**
   * Sets the turn angle of the robot.
   *
   * @return the angle at which the robot should turn
   */
  public int getTurnAngle() { return this.turnAngle; }
}
