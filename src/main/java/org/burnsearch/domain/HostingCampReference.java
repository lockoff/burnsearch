package org.burnsearch.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Information about a camp kept with the documents for events hosted by the camp.
 */
public class HostingCampReference {

  @Field(type = FieldType.Long)
  private Long id;

  @Field(type = FieldType.String, indexAnalyzer = "standard")
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

    HostingCampReference that = (HostingCampReference) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HostingCampReference{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
