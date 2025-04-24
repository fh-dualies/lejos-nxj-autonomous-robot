package event.base;

import io.command.ICommand;
import java.util.Objects;

public class CommandEvent extends AbstractEvent {
  private final ICommand command;

  public CommandEvent(ICommand command) {
    super();

    this.command = Objects.requireNonNull(command, "Command cannot be null");
  }

  public ICommand getCommand() { return this.command; }

  @Override
  public String toString() {
    return "CommandEvent{"
        + "command=" + this.command + ", timestamp=" + this.getTimestamp() + '}';
  }
}
