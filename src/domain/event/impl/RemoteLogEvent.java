package domain.event.impl;

import domain.event.base.AbstractEvent;
import domain.event.base.IExposableEvent;

/**
 * RemoteLogEvent is an event that represents a log message generated by the robot.
 * It extends the AbstractEvent class and contains a reference to the log message.
 */
public class RemoteLogEvent extends AbstractEvent implements IExposableEvent {
  /**
   * The log message that generated the event.
   */
  private final String message;

  /**
   * Constructor for the RemoteLogEvent class.
   *
   * @param message the log message that generated the event
   */
  public RemoteLogEvent(String message) {
    if (message == null || message.isEmpty()) {
      throw new IllegalArgumentException("Message cannot be null or empty");
    }

    this.message = message;
  }

  @Override
  public String toExposableString() {
    return "LOG|" + message;
  }
}
