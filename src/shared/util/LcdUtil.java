package shared.util;

import lejos.nxt.LCD;

/**
 * Utility class for displaying messages on the NXT LCD screen.
 * This class provides methods to print messages at specific positions
 * and to clear the screen.
 */
public final class LcdUtil {
  /**
   * Prints a message at the specified position on the LCD screen by using the Position enum.
   */
  public static void print(String message, Position position) {
    LCD.drawString(message, 0, position.ordinal() + 1);
    LCD.refresh();
  }

  /**
   * Prints a message at the specified line on the LCD screen.
   * The line number should be between 0 and 3.
   */
  public static void print(String message, int line) {
    LCD.drawString(message, 0, line);
    LCD.refresh();
  }

  /**
   * Clears the LCD screen.
   */
  public static void clear() { LCD.clear(); }

  /**
   * Enum representing the position on the LCD screen.
   */
  public enum Position {
    ERROR,
    INFO,
    STREAM,
    WARNING,
  }
}
