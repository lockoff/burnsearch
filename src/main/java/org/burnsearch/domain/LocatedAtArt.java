package org.burnsearch.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Location information for when an event is held at an art piece, kept with an event document.
 */
public class LocatedAtArt {
  @Field(type = FieldType.Long)
  private Long id;

  @Field(type = FieldType.String, indexAnalyzer = "standard")
  private String locationString;

  @Field(type = FieldType.String, indexAnalyzer = "whitespace")
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LocatedAtArt that = (LocatedAtArt) o;

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
    return "LocatedAtArt{" +
        "id=" + id +
        ", locationString='" + locationString + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
