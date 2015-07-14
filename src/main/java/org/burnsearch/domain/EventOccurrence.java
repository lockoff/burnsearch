package org.burnsearch.domain;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Information about the time an event occurs, kept with an event document.
 */
public class EventOccurrence {
  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd hh:mm:ss")
  private Date startTime;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd hh:mm:ss")
  private Date endTime;

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventOccurrence that = (EventOccurrence) o;

    if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null)
      return false;
    return !(endTime != null ? !endTime.equals(that.endTime) : that.endTime != null);

  }

  @Override
  public int hashCode() {
    int result = startTime != null ? startTime.hashCode() : 0;
    result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EventOccurrence{" +
        "startTime=" + startTime +
        ", endTime=" + endTime +
        '}';
  }
}
