package domain.event.impl;

import domain.event.base.AbstractEvent;

/**
 * ButtonEvent is an domain.event that represents a button press on the robot.
 * It extends the AbstractEvent class and contains a reference to the button ID that generated the domain.event.
 */
public class ButtonEvent extends AbstractEvent {
  /**
   * The ID of the button that generated the domain.event.
   */
  private final String buttonId;

  /**
   * Constructor that initializes the ButtonEvent with the button ID.
   *
   * @param buttonId the ID of the button that generated the domain.event
   */
  public ButtonEvent(String buttonId) {
    if (buttonId == null || buttonId.isEmpty()) {
      throw new IllegalArgumentException("Button ID cannot be null or empty");
    }

    this.buttonId = buttonId;
  }

  /**
   * Returns the ID of the button that generated the domain.event.
   *
   * @return the ID of the button that generated the domain.event
   */
  public String getButtonId() { return this.buttonId; }

  /**
   * Returns the string representation of the button domain.event.
   *
   * @return the string representation of the button domain.event
   */
  public String toExposableString() { return "BUTTON:" + this.buttonId; }
}
