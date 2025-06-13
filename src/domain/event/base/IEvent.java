package domain.event.base;

/**
 * IEvent is an interface that defines the methods for an domain.event.
 * It provides a method to retrieve the timestamp of the domain.event.
 */
public interface IEvent {
  /**
   * Returns the timestamp of the domain.event.
   *
   * @return the timestamp of the domain.event in milliseconds.
   */
  long getTimestamp();
}
