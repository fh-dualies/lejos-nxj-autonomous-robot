package event.base;

/**
 * This class represents an abstract event with a timestamp. This class is intended to be inherited by specific event
 * classes and later dispatched to the appropriate event handler.* It provides a constructor to initialize the timestamp
 * and a method to retrieve it.
 */
abstract public class AbstractEvent {
  /**
   * The timestamp of the event, representing the time when the event was created.
   */
  private final long timestamp;

  public AbstractEvent() { this.timestamp = System.currentTimeMillis(); }

  /**
   * @return the timestamp of the event in milliseconds.
   */
  public long getTimestamp() { return this.timestamp; }
}
