package util;

public final class Log {

  private static final String INFO_LEVEL = "INFO";
  private static final String WARNING_LEVEL = "WARNING";
  private static final String ERROR_LEVEL = "SEVERE";

  private static final String LOGGER_NAME = "util.Log";

  private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

  private Log() {}

  private static void log(String level, String message, Throwable thrown) {
    StringBuilder builder = new StringBuilder();
    long timeMillis = System.currentTimeMillis();

    builder.append(timeMillis);
    builder.append(" [").append(level);

    for (int i = level.length(); i < 7; i++) {
      builder.append(' ');
    }

    builder.append("] ");
    builder.append(LOGGER_NAME);
    builder.append(" - ");
    builder.append(message);
    java.io.PrintStream out = ERROR_LEVEL.equals(level) ? System.err : System.out;
    out.println(builder);

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
