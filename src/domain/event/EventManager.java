package domain.event;

import domain.event.base.AbstractEvent;
import domain.event.base.IEventListener;
import java.util.Vector;
import shared.util.Logger;

/**
 * EventManager is responsible for managing event listeners and dispatching events to them.
 * It allows adding and removing listeners and ensures thread-safe operations using CopyOnWriteArrayList.
 */
public class EventManager {
  /**
   * List of event listeners.
   * Using Vector for thread-safe operations (it's an older implementation of CopyOnWriteArrayList).
   */
  private final Vector<IEventListener> listeners = new Vector<>();

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

    for (int i = 0; i < this.listeners.size(); i++) {
      try {
        this.listeners.elementAt(i).onEvent(event);
      } catch (Exception e) {
        Logger.error("dispatching error: ", e);
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

    this.listeners.addElement(listener);
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

    boolean removed = this.listeners.removeElement(listener);

    if (!removed) {
      throw new IllegalArgumentException("Listener does not exist. State inconsistent.");
    }
  }

  /**
   * Returns the current number of registered listeners. Note: Due to the nature of CopyOnWriteArrayList, the size might
   * change immediately after this call if listeners are added or removed concurrently.
   *
   * @return the number of listeners.
   */
  public int getListenerCount() { return this.listeners.size(); }
}
