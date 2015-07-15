package org.burnsearch.service;

import org.burnsearch.Application;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

/**
 * Tests that the ETL service can actually hit the Playa Events API.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EtlServiceIntegrationTest {

  @Inject
  private EtlService etlService;

  @Inject
  private CampSearchRepository campSearchRepository;

  @Inject
  private EventSearchRepository eventSearchRepository;

  @Test
  public void testIndex() throws Exception {
    etlService.indexCampsAndEvents();
    assertTrue(eventSearchRepository.count() > 0);
    assertTrue(campSearchRepository.count() > 0);
  }
}
