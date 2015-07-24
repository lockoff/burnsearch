package org.burnsearch.web.rest;

import com.google.common.collect.Lists;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.EventSearchRepository;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;

import java.util.List;

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

  @SuppressWarnings("unchecked")
  @Test
  public void testSearchByDescription() throws Exception {
    String descriptionQuery = "Hello world.";
    QueryBuilder queryBuilder = new MatchQueryBuilder("description", descriptionQuery);
    PageRequest pageRequest = new PageRequest(0, 10);
    Event result1 = new Event();
    result1.setId(1L);
    result1.setDescription("Hello world.");
    Event result2 = new Event();
    result2.setId(1L);
    result2.setDescription("Goodbye world.");
    List<Event> expectedSearchResults = Lists.newArrayList(result1, result2);
    FacetedPage<Event> mockPage = (FacetedPage<Event>) mock(FacetedPage.class);

    ArgumentMatcher<QueryBuilder> queryMatcher = new ArgumentMatcher<QueryBuilder>() {
      @Override
      public boolean matches(Object o) {
        return o instanceof MatchQueryBuilder &&
            BytesReference.Helper.bytesEqual(queryBuilder.buildAsBytes(),
                ((MatchQueryBuilder) o).buildAsBytes());
      }
    };
    when(mockRepository.search(argThat(queryMatcher), eq(pageRequest))).thenReturn(mockPage);
    when(mockPage.getContent()).thenReturn(expectedSearchResults);
    when(mockPage.getNumber()).thenReturn(0);
    when(mockPage.getTotalPages()).thenReturn(10);

    List<Event> actualSearchResults =
        eventResource.searchByDescription(descriptionQuery, 0, 10)
        .getContent();
    assertEquals("Wrong results list.", expectedSearchResults, actualSearchResults);
  }
}
