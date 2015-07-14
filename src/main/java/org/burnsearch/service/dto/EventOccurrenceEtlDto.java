package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.Event;
import org.burnsearch.domain.EventOccurrence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * DTO recording the time an event occurs, retrieved from the Burning Man Events API.
 */
public class EventOccurrenceEtlDto {
  private static DateFormat OCCURRENCE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  @JsonProperty("start_time")
  private String startTime;

  @JsonProperty("end_time")
  private String endTime;

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public EventOccurrence toEventOccurrenceDocument() {
    try {
      EventOccurrence occurrence = new EventOccurrence();
      occurrence.setStartTime(OCCURRENCE_DATE_FORMAT.parse(startTime));
      occurrence.setEndTime(OCCURRENCE_DATE_FORMAT.parse(endTime));
      return occurrence;
    } catch (ParseException e) {
      throw new RuntimeException("Error parsing string containing event occurrene time. " +
          "Has the BM Events API date format changed?", e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventOccurrenceEtlDto that = (EventOccurrenceEtlDto) o;

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
    return "EventOccurrenceEtlDto{" +
        "startTime='" + startTime + '\'' +
        ", endTime='" + endTime + '\'' +
        '}';
  }
}
