package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.burnsearch.domain.Camp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * DTO for information about camps retrieved from the Burning Man Events API.
 */
public class CampEtlDto {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("description")
  private String description;

  @JsonProperty("url")
  private String url;

  @JsonProperty("contact_email")
  private String contactEmail;

  @JsonProperty("year")
  private YearEtlDto year;

  @JsonProperty("name")
  private String name;

  public Camp toCampDocument() {
    Camp camp = new Camp();
    camp.setId(id);
    camp.setName(name);
    camp.setDescription(description);
    camp.setUrl(url);
    camp.setContactEmail(contactEmail);
    camp.setYear(year.toYearDate());
    return camp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public YearEtlDto getYear() {
    return year;
  }

  public void setYear(YearEtlDto year) {
    this.year = year;
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

    CampEtlDto that = (CampEtlDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null)
      return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;
    if (contactEmail != null ? !contactEmail.equals(that.contactEmail) : that.contactEmail != null)
      return false;
    if (year != null ? !year.equals(that.year) : that.year != null) return false;
    return !(name != null ? !name.equals(that.name) : that.name != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
    result = 31 * result + (year != null ? year.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CampEtlDto{" +
        "id=" + id +
        ", description='" + description + '\'' +
        ", url='" + url + '\'' +
        ", contactEmail='" + contactEmail + '\'' +
        ", year=" + year +
        ", name='" + name + '\'' +
        '}';
  }
}
