package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {
  private static final Logger LOGGER;

  static {
    LOGGER = Logger.getLogger(Log.class.getName());

    LOGGER.setUseParentHandlers(false);

    LogFormatter formatter = new LogFormatter();
    ConsoleHandler consoleHandler = new ConsoleHandler();

    consoleHandler.setFormatter(formatter);
    consoleHandler.setLevel(java.util.logging.Level.ALL);

    LOGGER.addHandler(consoleHandler);
    LOGGER.setLevel(Level.ALL);
    LOGGER.config("Log configuration loaded.");
  }

  public static void info(String message) {
    if (LOGGER.isLoggable(Level.INFO)) {
      LOGGER.log(Level.INFO, message);
    }
  }

  public static void warning(String message) {
    if (LOGGER.isLoggable(Level.WARNING)) {
      LOGGER.log(Level.WARNING, message);
    }
  }

  public static void warning(String message, Throwable thrown) {
    if (LOGGER.isLoggable(Level.WARNING)) {
      LOGGER.log(Level.WARNING, message, thrown);
    }
  }

  public static void error(String message) {
    if (LOGGER.isLoggable(Level.SEVERE)) {
      LOGGER.log(Level.SEVERE, message);
    }
  }

  public static void error(String message, Throwable thrown) {
    if (LOGGER.isLoggable(Level.SEVERE)) {
      LOGGER.log(Level.SEVERE, message, thrown);
    }
  }
}
