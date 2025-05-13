package event;

import event.base.AbstractEvent;
import event.base.IExposableEvent;

public class RemoteLogEvent extends AbstractEvent implements IExposableEvent {
  private final String message;

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
