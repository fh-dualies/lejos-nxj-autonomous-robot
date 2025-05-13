package util;

public class SystemMonitor {
  public static void readMemoryUsage( ) {
    Runtime runtime = Runtime.getRuntime();

    long totalMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();

    Log.info(formatBytes(totalMemory - freeMemory) + "/" + formatBytes(totalMemory));
  }

  public static String formatBytes(long bytes) {
    if (bytes < 1024) {
      return bytes + "B";
    }

    int exp = (int) (Math.log(bytes) / Math.log(1024));
    String pre = "KMGTPE".charAt(exp-1) + "";

    return bytes / Math.pow(1024, exp) + " " + pre;
  }
}
