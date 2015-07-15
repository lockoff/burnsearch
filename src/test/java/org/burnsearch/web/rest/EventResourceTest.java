package org.burnsearch.web.rest;

import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.EventSearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventResourceTest {

  @Mock
  private EventSearchRepository mockRepository;

  @InjectMocks
  private EventResource eventResource;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetEventById() throws Exception {
    Event expectedEvent = new Event();
    expectedEvent.setId(1L);

    when(mockRepository.findOne(1L)).thenReturn(expectedEvent);

    Event actualEvent = eventResource.getEventById(1L);
    assertEquals("Endpoint returns wrong event.", expectedEvent, actualEvent);
  }
}
