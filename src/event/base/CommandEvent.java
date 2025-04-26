package event.base;

import io.command.ICommand;
import java.util.Objects;

/**
 * CommandEvent is an event that represents a command that has to be executed in the system.
 * More specifically, the commands are created by the user connected via bluetooth and e.g., want to control the
 * movement or speed of the robot. It extends the AbstractEvent class and contains a reference to the command being
 * executed.
 */
public class CommandEvent extends AbstractEvent {
  /**
   * The command that has to be executed in the system.
   */
  private final ICommand command;

  /**
   * @param command the command that has to be executed in the system
   */
  public CommandEvent(ICommand command) {
    super();

    this.command = Objects.requireNonNull(command, "Command cannot be null");
  }

  /**
   * @return the command that has to be executed in the system
   */
  public ICommand getCommand() { return this.command; }

  @Override
  public String toString() {
    return "CommandEvent{"
        + "command=" + this.command + ", timestamp=" + this.getTimestamp() + '}';
  }
}
