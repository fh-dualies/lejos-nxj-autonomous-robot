package io.sensor.reader;

import io.sensor.SensorType;

public interface ISensorReader {
  String getSensorId();

  SensorType getSensorType();

  int checkValue();

  int getLastValue();
}
