package util;

import main.Config;

/**
 * The SystemMonitor class is responsible for monitoring the system's memory usage and logging it
 * periodically. It provides a method to log the current memory usage in a human-readable format.
 */
public final class SystemMonitor {
  /**
   * The delay in milliseconds between memory usage logs. This controls how frequently the memory
   * usage is logged.
   */
  private static final int LOG_DELAY = Config.SYSTEM_MONITOR_LOG_DELAY.getIntValue();

  /**
   * The last time the memory usage was logged. This is used to ensure that logs are not generated
   * too frequently.
   */
  private static long lastLogTime = 0;

  /**
   * Logs the current memory usage of the system. It calculates the used and total memory and logs
   * them in a human-readable format.
   */
  public static void logMemoryUsage() {
    long now = System.currentTimeMillis();

    if (now - lastLogTime < LOG_DELAY) {
      return;
    }

    lastLogTime = now;

    Runtime runtime = Runtime.getRuntime();
    long used = runtime.totalMemory() - runtime.freeMemory();
    long total = runtime.totalMemory();

    Log.info(formatBytes(used) + "/" + formatBytes(total));
  }

  /**
   * Formats the given number of bytes into a human-readable string. It converts the byte count into
   * KB, MB, GB, etc., depending on the size.
   *
   * @param bytes The number of bytes to format.
   * @return A string representing the formatted byte count.
   */
  private static String formatBytes(long bytes) {
    if (bytes < 1024) {
      return bytes + "B";
    }

    int exp = (int)(Math.log(bytes) / Math.log(1024));
    String unit = "KMGTPE".charAt(exp - 1) + "B";

    double value = bytes / Math.pow(1024, exp);
    double rounded = Math.round(value * 10) / 10.0;

    return rounded + " " + unit;
  }
}