package shared.util;

import app.RoboApplication;
import domain.event.EventManager;
import domain.event.impl.RemoteLogEvent;

/**
 * The Log class provides a simple logging utility for the application.
 * It allows logging messages at different levels (INFO, WARNING, ERROR)
 * and can also log exceptions. The logs are printed to the standard
 * output or error stream and can be dispatched to an event manager
 * if one is set.
 */
public final class Logger {
  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "SEVERE";
  private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
  private static EventManager eventManager = null;

  public static void setEventManager(EventManager eventManager) { Logger.eventManager = eventManager; }

  private static void log(String level, String message, Throwable thrown) {
    if (!RoboApplication.DEBUG) {
      return;
    }

    StringBuilder builder = new StringBuilder();
    long timeMillis = System.currentTimeMillis();

    builder.append(timeMillis);
    builder.append(" [").append(level);
    builder.append("] ");
    builder.append(message);
    builder.append(LINE_SEPARATOR);

    java.io.PrintStream out = ERROR_LEVEL.equals(level) ? System.err : System.out;
    String msg = builder.toString();
    out.println(msg);

    if (Logger.eventManager != null) {
      Logger.eventManager.dispatch(new RemoteLogEvent(msg));
    }

    if (thrown != null) {
      out.print("Stack trace for previous log entry:" + LINE_SEPARATOR);
      thrown.printStackTrace(out);
    }
  }

  /**
   * Logs an informational message.
   *
   * @param message The message to log.
   */
  public static void info(String message) { log(INFO_LEVEL, message, null); }

  /**
   * Logs a warning message.
   *
   * @param message The message to log.
   */
  public static void warning(String message) { log(WARNING_LEVEL, message, null); }

  /**
   * Logs an error message.
   *
   * @param message The message to log.
   */
  public static void warning(String message, Throwable thrown) { log(WARNING_LEVEL, message, thrown); }

  /**
   * Logs an error message.
   *
   * @param message The message to log.
   */
  public static void error(String message) { log(ERROR_LEVEL, message, null); }

  /**
   * Logs an error message with an exception.
   *
   * @param message The message to log.
   * @param thrown  The exception to log.
   */
  public static void error(String message, Throwable thrown) { log(ERROR_LEVEL, message, thrown); }
}
