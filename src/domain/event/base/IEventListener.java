package domain.event.base;

/**
 * Interface for domain.event listeners.
 */
public interface IEventListener {
  /**
   * Method to handle events.
   *
   * @param event the domain.event to handle
   */
  void onEvent(AbstractEvent event);
}
