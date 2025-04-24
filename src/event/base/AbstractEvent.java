package event.base;

abstract public class AbstractEvent {
  private final long timestamp;

  public AbstractEvent() { this.timestamp = System.currentTimeMillis(); }

  public long getTimestamp() { return this.timestamp; }
}
