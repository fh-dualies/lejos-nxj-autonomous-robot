package event.base;

/**
 * This interface represents an event that can be exposed to the external system.
 * It will be sent automatically to the external system when the event is created.
 */
public interface IExposableEvent {
  /**
   * @return the string representation of the string that will be sent to the external system
   */
  String toExposableString();
}
