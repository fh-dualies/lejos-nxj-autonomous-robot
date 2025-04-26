package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  private final Date date = new Date();

  @Override
  public String format(LogRecord record) {
    StringBuilder builder = new StringBuilder();

    synchronized (date) {
      date.setTime(record.getMillis());
      builder.append(dateFormat.format(date));
    }

    builder.append(" [").append(record.getThreadID()).append("] ");

    builder.append(String.format("%-7s", record.getLevel().getName()));

    builder.append(" ");
    String className = record.getSourceClassName();

    if (className != null) {
      int lastDot = className.lastIndexOf(".");

      if (lastDot > 0 && lastDot < className.length() - 1) {
        builder.append(className.substring(lastDot + 1));
      } else {
        builder.append(className);
      }
    } else {
      builder.append(record.getLoggerName());
    }

    builder.append(" - ");
    builder.append(formatMessage(record));

    builder.append(System.lineSeparator());

    Throwable thrown = record.getThrown();

    if (thrown != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        thrown.printStackTrace(pw);
        pw.close();
        builder.append(sw);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    return builder.toString();
  }
}
