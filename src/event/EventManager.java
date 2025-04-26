package event;

import event.base.AbstractEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventManager is responsible for managing event listeners and dispatching events to them.
 * It allows adding and removing listeners, and ensures thread-safe operations using CopyOnWriteArrayList.
 */
public class EventManager {
  /**
   * List of event listeners.
   * Using CopyOnWriteArrayList for thread-safe operations.
   * Maybe replaced with a more efficient solution if needed.
   */
  private final List<IEventListener> listeners = new CopyOnWriteArrayList<>();

  /**
   * Dispatches the given event to all registered listeners.
   *
   * @param event the event to dispatch
   * @throws IllegalArgumentException if the event is null
   */
  public void dispatch(AbstractEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Event cannot be null");
    }

    for (IEventListener listener : listeners) {
      try {
        listener.onEvent(event);
      } catch (Exception e) {
        System.err.println("Error while dispatching event: " + e.getMessage());
        throw e;
      }
    }
  }

  /**
   * Adds a new listener to the list of listeners.
   *
   * @param listener the listener to add
   * @throws IllegalArgumentException if the listener is null or already exists
   */
  public synchronized void addListener(IEventListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }

    if (this.listeners.contains(listener)) {
      throw new IllegalArgumentException("Listener already exists");
    }

    this.listeners.add(listener);
  }

  /**
   * Removes a listener from the list of listeners.
   *
   * @param listener the listener to remove
   * @throws IllegalArgumentException if the listener is null or does not exist
   */
  public synchronized void removeListener(IEventListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }

    if (!this.listeners.contains(listener)) {
      throw new IllegalArgumentException("Listener does not exist");
    }

    this.listeners.remove(listener);
  }
}
