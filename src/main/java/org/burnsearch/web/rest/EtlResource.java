package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.security.AuthoritiesConstants;
import org.burnsearch.service.EtlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;

/**
 * Endpoint for triggering the ETL process.
 */
@RestController
@RequestMapping("/api")
public class EtlResource {
  private static final Logger LOG = LoggerFactory.getLogger(EtlResource.class);

  @Inject
  private EtlService etlService;

  @RequestMapping(value = "/admin/etl", method = RequestMethod.POST)
  @Timed
  @RolesAllowed(AuthoritiesConstants.ADMIN)
  public void etl() throws IOException, ParseException {
    LOG.info("Starting ETL of Camps and Events");
    etlService.indexCampsAndEvents();
    LOG.info("ETL complete.");
  }
}
