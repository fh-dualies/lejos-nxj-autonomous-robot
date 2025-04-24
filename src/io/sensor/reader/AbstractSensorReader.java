package io.sensor.reader;

import event.EventManager;
import event.base.SensorEvent;
import io.sensor.SensorType;
import java.util.Objects;

public abstract class AbstractSensorReader {
  protected final EventManager eventManager;
  protected int lastValue = -1;
  protected static final int DEFAULT_REPORT_THRESHOLD = 10;

  public AbstractSensorReader(EventManager eventManager) { this.eventManager = Objects.requireNonNull(eventManager); }

  public int checkValue() {
    int currentValue = this.readSensorValue();

    if (this.lastValue == -1 || Math.abs(currentValue - this.lastValue) >= this.getReportThreshold()) {
      this.lastValue = currentValue;
      this.eventManager.dispatch(new SensorEvent(this.getSensorId(), this.getSensorType(), currentValue));
    }

    return currentValue;
  }

  public int getLastValue() { return this.lastValue; }

  abstract String getSensorId();

  abstract SensorType getSensorType();

  protected abstract int readSensorValue();

  protected int getReportThreshold() { return DEFAULT_REPORT_THRESHOLD; }
}