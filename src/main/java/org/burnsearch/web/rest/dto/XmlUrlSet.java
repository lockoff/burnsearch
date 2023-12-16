package org.burnsearch.web.rest.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "urlset")
public class XmlUrlSet {

  @XmlElements({@XmlElement(name = "url", type = XmlUrl.class)})
  private Collection<XmlUrl> xmlUrls = new ArrayList<XmlUrl>();

  public void addUrl(XmlUrl xmlUrl) {
    xmlUrls.add(xmlUrl);
  }

  public Collection<XmlUrl> getXmlUrls() {
    return xmlUrls;
  }

  public void clearUrls() {
    xmlUrls.clear();
  }
}