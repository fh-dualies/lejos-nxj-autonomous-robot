package io.command;

import java.util.Objects;

/**
 * MoveCommand represents a command to move the robot in a specific direction.
 * It encapsulates the direction of movement and provides a method to retrieve it.
 */
public class MoveCommand implements ICommand {
  /**
   * The direction in which the robot should move.
   */
  private final Direction direction;

  /**
   * @param direction the direction in which the robot should move
   * @throws NullPointerException if the direction is null
   */
  public MoveCommand(Direction direction) { this.direction = Objects.requireNonNull(direction); }

  /**
   * @return the direction in which the robot should move
   */
  public Direction getDirection() { return this.direction; }

  /**
   * Direction enum represents the possible directions the robot can move.
   */
  public enum Direction { FORWARD, BACKWARD, LEFT, RIGHT, STOP }
}
