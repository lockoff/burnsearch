package org.burnsearch.service;

import org.burnsearch.Application;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

/**
 * Tests that the ETL service can actually hit the Playa Events API.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Category(IntegrationTest.class)
public class EtlServiceIntegrationTest {

  @Inject
  private EtlService etlService;

  @Test
  public void testIndex() throws Exception {
    etlService.indexCampsAndEvents();
  }
}
