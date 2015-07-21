package org.burnsearch.domain;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

/**
 * A camp at the Burning Man event.
 *
 * A particular camp is present at a year of the event. Camps must have names and descriptions, and
 * may have contact information in the form of a URL or contact email address.
 */
@Document(indexName = "burn", type = "camp", shards = 1, refreshInterval = "-1")
public class Camp {
  @Id
  private Long id;

  @Field(type = FieldType.String, indexAnalyzer = "whitespace", searchAnalyzer = "whitespace")
  private String name;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String url;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String contactEmail;

  @Field(type = FieldType.String, indexAnalyzer = "english", searchAnalyzer = "english")
  private String description;

  @Field(type = FieldType.Date, format = DateFormat.year)
  private Date year;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getYear() {
    return year;
  }

  public void setYear(Date year) {
    this.year = year;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Camp camp = (Camp) o;

    if (!id.equals(camp.id)) return false;
    if (!name.equals(camp.name)) return false;
    if (url != null ? !url.equals(camp.url) : camp.url != null) return false;
    if (contactEmail != null ? !contactEmail.equals(camp.contactEmail) : camp.contactEmail != null)
      return false;
    if (!description.equals(camp.description)) return false;
    return year.equals(camp.year);

  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
    result = 31 * result + description.hashCode();
    result = 31 * result + year.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Camp{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", url='" + url + '\'' +
        ", contactEmail='" + contactEmail + '\'' +
        ", description='" + description + '\'' +
        ", year='" + year + '\'' +
        '}';
  }
}
