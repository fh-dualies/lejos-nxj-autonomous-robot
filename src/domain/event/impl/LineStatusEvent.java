package domain.event.impl;

import domain.event.base.AbstractEvent;
import domain.event.base.IExposableEvent;

/**
 * LineStatusEvent is an event that represents the status of a line (on or off).
 * It extends the AbstractEvent class and implements the IExposableEvent interface.
 */
public class LineStatusEvent extends AbstractEvent implements IExposableEvent {
  /**
   * Indicates whether the line is currently on or off.
   */
  private final boolean onLine;

  /**
   * Constructor that initializes the LineStatusEvent with the line status.
   *
   * @param onLine true if the line is on, false if it is off
   */
  public LineStatusEvent(boolean onLine) { this.onLine = onLine; }

  /**
   * Returns whether the line is currently on.
   *
   * @return true if the line is on, false if it is off
   */
  public boolean isOnLine() { return onLine; }

  /**
   * Returns the string representation of the line status event.
   *
   * @return the string representation of the line status event
   */
  @Override
  public String toExposableString() {
    return "LINE_STATUS|" + (onLine ? "ON" : "OFF");
  }
}
