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
   * @return the speed at which the robot should move
   */
  public int getSpeed() { return this.speed; }

  /**
   * @return the angle at which the robot should turn
   */
  public int getTurnAngle() { return this.turnAngle; }
}
