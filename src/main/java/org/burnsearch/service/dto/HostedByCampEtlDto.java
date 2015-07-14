package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.HostingCampReference;

/**
 * DTL for information about a camp hosting an event retrieved from the Burning Man Events API.
 */
public class HostedByCampEtlDto {
  @JsonProperty("name")
  private String name;

  @JsonProperty("id")
  private Long id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HostingCampReference toHostingCampReferenceDocument() {
    HostingCampReference reference = new HostingCampReference();
    reference.setId(id);
    reference.setName(name);
    return reference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HostedByCampEtlDto that = (HostedByCampEtlDto) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return !(id != null ? !id.equals(that.id) : that.id != null);

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HostedByCampEtlDto{" +
        "name='" + name + '\'' +
        ", id=" + id +
        '}';
  }
}
