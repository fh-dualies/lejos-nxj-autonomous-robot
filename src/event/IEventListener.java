package event;

import event.base.AbstractEvent;

/**
 * Interface for event listeners.
 */
public interface IEventListener {
  /**
   * Method to handle events.
   *
   * @param event the event to handle
   */
  void onEvent(AbstractEvent event);
}
