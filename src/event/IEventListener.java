package event;

import event.base.AbstractEvent;

public interface IEventListener {
  void onEvent(AbstractEvent event);
}
