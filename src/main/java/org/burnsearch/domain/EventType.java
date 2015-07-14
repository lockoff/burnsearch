package org.burnsearch.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Information about the type of an event, kept with an event document.
 */
public class EventType {
  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String abbreviation;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String label;

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

    EventType eventType = (EventType) o;

    if (abbreviation != null ? !abbreviation.equals(eventType.abbreviation) : eventType.abbreviation != null)
      return false;
    return !(label != null ? !label.equals(eventType.label) : eventType.label != null);

  }

  @Override
  public int hashCode() {
    int result = abbreviation != null ? abbreviation.hashCode() : 0;
    result = 31 * result + (label != null ? label.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EventType{" +
        "abbreviation='" + abbreviation + '\'' +
        ", label='" + label + '\'' +
        '}';
  }
}
