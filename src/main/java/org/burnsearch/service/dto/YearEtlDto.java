package org.burnsearch.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DTO for year information carried by objects returned by the Burning Man Events API.
 */
public class YearEtlDto {
  private static final DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");

  @JsonProperty("id")
  private Long id;

  @JsonProperty("year")
  private String year;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public Date toYearDate() {
    try {
      return YEAR_FORMAT.parse(year);
    } catch (ParseException e) {
      throw new RuntimeException("Error parsing string containing year into date. Has the BM Events API changed?", e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    YearEtlDto that = (YearEtlDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return !(year != null ? !year.equals(that.year) : that.year != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (year != null ? year.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "YearEtlDto{" +
        "id=" + id +
        ", year='" + year + '\'' +
        '}';
  }
}
