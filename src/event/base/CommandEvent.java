package event.base;

import io.command.ICommand;

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
    if (command == null) {
      throw new NullPointerException();
    }

    this.command = command;
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
