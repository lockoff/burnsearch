package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for information about events retrieved from the Burning Man Events API.
 */
public class EventEtlDto {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("hosted_by_camp")
  private HostedByCampEtlDto hostedByCamp;

  @JsonProperty("occurrence_set")
  private List<EventOccurrenceEtlDto> occurrenceSet;

  @JsonProperty("description")
  private String description;

  @JsonProperty("print_description")
  private String printDescription;

  @JsonProperty("title")
  private String title;

  @JsonProperty("url")
  private String url;

  @JsonProperty("year")
  private YearEtlDto year;

  @JsonProperty("all_day")
  private boolean allDay;

  @JsonProperty("check_location")
  private boolean checkLocation;

  @JsonProperty("slug")
  private String slug;

  @JsonProperty("other_location")
  private String otherLocation;

  @JsonProperty("event_type")
  private EventTypeEtlDto eventType;

  @JsonProperty("located_at_art")
  private LocatedAtArtEtlDto locatedAtArt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HostedByCampEtlDto getHostedByCamp() {
    return hostedByCamp;
  }

  public void setHostedByCamp(HostedByCampEtlDto hostedByCamp) {
    this.hostedByCamp = hostedByCamp;
  }

  public List<EventOccurrenceEtlDto> getOccurrenceSet() {
    return occurrenceSet;
  }

  public void setOccurrenceSet(List<EventOccurrenceEtlDto> occurrenceSet) {
    this.occurrenceSet = occurrenceSet;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPrintDescription() {
    return printDescription;
  }

  public void setPrintDescription(String printDescription) {
    this.printDescription = printDescription;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public YearEtlDto getYear() {
    return year;
  }

  public void setYear(YearEtlDto year) {
    this.year = year;
  }

  public boolean isAllDay() {
    return allDay;
  }

  public void setAllDay(boolean allDay) {
    this.allDay = allDay;
  }

  public boolean isCheckLocation() {
    return checkLocation;
  }

  public void setCheckLocation(boolean checkLocation) {
    this.checkLocation = checkLocation;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getOtherLocation() {
    return otherLocation;
  }

  public void setOtherLocation(String otherLocation) {
    this.otherLocation = otherLocation;
  }

  public EventTypeEtlDto getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEtlDto eventType) {
    this.eventType = eventType;
  }

  public LocatedAtArtEtlDto getLocatedAtArt() {
    return locatedAtArt;
  }

  public void setLocatedAtArt(LocatedAtArtEtlDto locatedAtArt) {
    this.locatedAtArt = locatedAtArt;
  }

  public Event toEventDocument() {
    Event event = new Event();
    event.setId(id);
    if (hostedByCamp != null) {
      event.setHostingCamp(hostedByCamp.toHostingCampReferenceDocument());
    }
    event.setOccurrenceSet(
        occurrenceSet
            .stream()
            .map(EventOccurrenceEtlDto::toEventOccurrenceDocument)
            .collect(Collectors.toCollection(ArrayList::new))
    );
    event.setDescription(description);
    event.setPrintDescription(printDescription);
    event.setTitle(title);
    event.setUrl(url);
    event.setYear(year.toYearDate());
    event.setIsAllDay(allDay);
    event.setIsCheckLocation(checkLocation);
    event.setSlug(slug);
    event.setOtherLocation(otherLocation);
    event.setEventType(eventType.toEventTypeDocument());
    if (locatedAtArt != null) {
      event.setLocatedAtArt(locatedAtArt.toLocatedAtArtDocument());
    }
    return event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    EventEtlDto that = (EventEtlDto) o;

    if (allDay != that.allDay) return false;
    if (checkLocation != that.checkLocation) return false;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (hostedByCamp != null ? !hostedByCamp.equals(that.hostedByCamp) : that.hostedByCamp != null)
      return false;
    if (occurrenceSet != null ? !occurrenceSet.equals(that.occurrenceSet) : that.occurrenceSet != null)
      return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (printDescription != null ? !printDescription.equals(that.printDescription) : that.printDescription != null)
      return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;
    if (year != null ? !year.equals(that.year) : that.year != null) return false;
    if (slug != null ? !slug.equals(that.slug) : that.slug != null) return false;
    if (otherLocation != null ? !otherLocation.equals(that.otherLocation) : that.otherLocation != null)
      return false;
    if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null)
      return false;
    return !(locatedAtArt != null ? !locatedAtArt.equals(that.locatedAtArt) : that.locatedAtArt != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (hostedByCamp != null ? hostedByCamp.hashCode() : 0);
    result = 31 * result + (occurrenceSet != null ? occurrenceSet.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (printDescription != null ? printDescription.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (year != null ? year.hashCode() : 0);
    result = 31 * result + (allDay ? 1 : 0);
    result = 31 * result + (checkLocation ? 1 : 0);
    result = 31 * result + (slug != null ? slug.hashCode() : 0);
    result = 31 * result + (otherLocation != null ? otherLocation.hashCode() : 0);
    result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
    result = 31 * result + (locatedAtArt != null ? locatedAtArt.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EventEtlDto{" +
        "id=" + id +
        ", hostedByCamp=" + hostedByCamp +
        ", occurrenceSet=" + occurrenceSet +
        ", description='" + description + '\'' +
        ", printDescription='" + printDescription + '\'' +
        ", title='" + title + '\'' +
        ", url='" + url + '\'' +
        ", year=" + year +
        ", allDay=" + allDay +
        ", checkLocation=" + checkLocation +
        ", slug='" + slug + '\'' +
        ", otherLocation='" + otherLocation + '\'' +
        ", eventType=" + eventType +
        ", locatedAtArt=" + locatedAtArt +
        '}';
  }
}
