package domain.event.base;

/**
 * This class represents an abstract domain.event with a timestamp. This class is intended to be inherited by specific
 * domain.event classes and later dispatched to the appropriate domain.event handler.* It provides a constructor to
 * initialize the timestamp and a method to retrieve it.
 */
abstract public class AbstractEvent implements IEvent {
  /**
   * The timestamp of the domain.event, representing the time when the domain.event was created.
   */
  private final long timestamp;

  /**
   * Constructor that initializes the timestamp of the domain.event to the current system time in milliseconds.
   */
  public AbstractEvent() { this.timestamp = System.currentTimeMillis(); }

  /**
   * Returns the timestamp of the domain.event.
   *
   * @return the timestamp of the domain.event in milliseconds.
   */
  @Override
  public long getTimestamp() {
    return this.timestamp;
  }
}
