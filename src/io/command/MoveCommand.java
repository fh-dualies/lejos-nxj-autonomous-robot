package io.command;

import java.util.Objects;

public class MoveCommand implements ICommand {
  public enum Direction { FORWARD, BACKWARD, LEFT, RIGHT, STOP }

  private final Direction direction;

  public MoveCommand(Direction direction) { this.direction = Objects.requireNonNull(direction); }

  public Direction getDirection() { return this.direction; }
}
