package event.abstracts;

/**
 * This class represents an abstract event with a timestamp. This class is intended to be inherited by specific event
 * classes and later dispatched to the appropriate event handler.* It provides a constructor to initialize the timestamp
 * and a method to retrieve it.
 */
abstract public class AbstractEvent implements IEvent {
  /**
   * The timestamp of the event, representing the time when the event was created.
   */
  private final long timestamp;

  /**
   * Constructor that initializes the timestamp of the event to the current system time in milliseconds.
   */
  public AbstractEvent() { this.timestamp = System.currentTimeMillis(); }

  /**
   * Returns the timestamp of the event.
   *
   * @return the timestamp of the event in milliseconds.
   */
  @Override
  public long getTimestamp() {
    return this.timestamp;
  }
}
