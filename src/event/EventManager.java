package event;

import event.base.AbstractEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
  private final List<IEventListener> listeners = new CopyOnWriteArrayList<>();

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

  public synchronized void addListener(IEventListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }

    if (this.listeners.contains(listener)) {
      throw new IllegalArgumentException("Listener already exists");
    }

    this.listeners.add(listener);
  }

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
