package event.base;

public abstract class AbstractEvent {
  private final long timestamp;

  public AbstractEvent() { this.timestamp = System.currentTimeMillis(); }

  public long getTimestamp() { return this.timestamp; }
}
