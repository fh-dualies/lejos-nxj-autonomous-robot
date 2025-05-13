package util;

import event.EventManager;
import event.RemoteLogEvent;
import main.RoboApplication;

public class Log {
  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "SEVERE";
  private static EventManager eventManager = null;

  public static void setEventManager(EventManager eventManager) {
    Log.eventManager = eventManager;
  }

  private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

  private static void log(String level, String message, Throwable thrown) {
    if (!RoboApplication.DEBUG) {
      return;
    }

    StringBuilder builder = new StringBuilder();
    long timeMillis = System.currentTimeMillis();

    builder.append(timeMillis);
    builder.append(" [").append(level);

    for (int i = level.length(); i < 7; i++) {
      builder.append(' ');
    }

    builder.append("] ");
    builder.append(message);
    builder.append(LINE_SEPARATOR);
    java.io.PrintStream out = ERROR_LEVEL.equals(level) ? System.err : System.out;
    String msg = builder.toString();
    out.println(msg);

    if (Log.eventManager != null) {
      Log.eventManager.dispatch(new RemoteLogEvent(msg));
    }

    if (thrown != null) {
      out.print("Stack trace for previous log entry:" + LINE_SEPARATOR);
      thrown.printStackTrace(out);
    }
  }

  public static void info(String message) { log(INFO_LEVEL, message, null); }

  public static void warning(String message) { log(WARNING_LEVEL, message, null); }

  public static void warning(String message, Throwable thrown) { log(WARNING_LEVEL, message, thrown); }

  public static void error(String message) { log(ERROR_LEVEL, message, null); }

  public static void error(String message, Throwable thrown) { log(ERROR_LEVEL, message, thrown); }
}
