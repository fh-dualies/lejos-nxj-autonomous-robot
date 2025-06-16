package domain.event.base;

/**
 * IEvent is an interface that defines the methods for an event.
 * It provides a method to retrieve the timestamp of the event.
 */
public interface IEvent {
  /**
   * Returns the timestamp of the event.
   *
   * @return the timestamp of the event in milliseconds.
   */
  long getTimestamp();
}
