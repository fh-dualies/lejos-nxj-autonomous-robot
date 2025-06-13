package domain.event.base;

/**
 * This interface represents an domain.event that can be exposed to the external system.
 * It will be sent automatically to the external system when the domain.event is created.
 */
public interface IExposableEvent {
  /**
   * Returns the string representation of the domain.event that will be sent to the external system.
   *
   * @return the string representation of the string that will be sent to the external system
   */
  String toExposableString();
}
