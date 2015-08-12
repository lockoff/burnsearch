package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.web.rest.dto.XmlUrl;
import org.burnsearch.web.rest.dto.XmlUrlSet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SiteMapResource {
  private static final String WEBSITE_URL = "https://www.playaplanner.org/";
  // Set by the ETL process.
  private static XmlUrlSet SITE_URLS = new XmlUrlSet();

  @RequestMapping(value = "/sitemap",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_XML_VALUE)
  @Timed
  public XmlUrlSet getSiteMap() {
    return SITE_URLS;
  }

  public static void addCampUrl(Long campId) {
    SITE_URLS.addUrl(new XmlUrl(WEBSITE_URL + "camp/" + campId.toString(),
        XmlUrl.Priority.MEDIUM));
  }

  public static void addEventUrl(Long eventId) {
    SITE_URLS.addUrl(new XmlUrl(WEBSITE_URL + "event/" + eventId.toString(),
        XmlUrl.Priority.MEDIUM));
  }

  public static void clearUrls() {
    SITE_URLS.clearUrls();
  }
}
