package util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtil {
  public static String[] split(String str, String delimiter) {
    if (str == null || delimiter == null) {
      return new String[0];
    }

    List<String> list = new ArrayList<>();

    int start = 0;
    int index = 0;

    while ((index = str.indexOf(delimiter, start)) != -1) {
      list.add(str.substring(start, index));
      start = index + delimiter.length();
    }

    list.add(str.substring(start));

    String[] result = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static String[] split(String str, String delimiter, int limit) {
    if (str == null || delimiter == null) {
      return new String[0];
    }

    if (limit <= 1) {
      String[] result = new String[1];
      result[0] = str;
      return result;
    }

    List<String> list = new ArrayList<>();

    int start = 0;
    int index = 0;

    while ((index = str.indexOf(delimiter, start)) != -1) {
      list.add(str.substring(start, index));
      start = index + delimiter.length();
      if (list.size() == limit - 1) {
        break;
      }
    }

    list.add(str.substring(start));

    String[] result = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }
}
