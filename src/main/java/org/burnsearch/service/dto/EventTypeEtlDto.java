package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.EventType;

/**
 * DTO carrying event type information retrieved for events from the Burning Man Events API.
 */
public class EventTypeEtlDto {
  @JsonProperty("abbr")
  private String abbreviation;

  @JsonProperty("label")
  private String label;

  public EventType toEventTypeDocument() {
    EventType eventType = new EventType();
    eventType.setAbbreviation(abbreviation);
    eventType.setLabel(label);
    return eventType;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventTypeEtlDto that = (EventTypeEtlDto) o;

    if (abbreviation != null ? !abbreviation.equals(that.abbreviation) : that.abbreviation != null)
      return false;
    return !(label != null ? !label.equals(that.label) : that.label != null);

  }

  @Override
  public int hashCode() {
    int result = abbreviation != null ? abbreviation.hashCode() : 0;
    result = 31 * result + (label != null ? label.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EventTypeEtlDto{" +
        "abbreviation='" + abbreviation + '\'' +
        ", label='" + label + '\'' +
        '}';
  }
}
