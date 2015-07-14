package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.LocatedAtArt;

/**
 * DTO carrying location information for events located at art pieces, as retrieved from the
 * Burning Man Events API.
 */
public class LocatedAtArtEtlDto {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("location_string")
  private String locationString;

  @JsonProperty("name")
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLocationString() {
    return locationString;
  }

  public void setLocationString(String locationString) {
    this.locationString = locationString;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocatedAtArt toLocatedAtArtDocument() {
    LocatedAtArt locatedAtArt = new LocatedAtArt();
    locatedAtArt.setId(id);
    locatedAtArt.setLocationString(locationString);
    locatedAtArt.setName(name);
    return locatedAtArt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LocatedAtArtEtlDto that = (LocatedAtArtEtlDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (locationString != null ? !locationString.equals(that.locationString) : that.locationString != null)
      return false;
    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (locationString != null ? locationString.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "LocatedAtArtEtlDto{" +
        "id=" + id +
        ", locationString='" + locationString + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
