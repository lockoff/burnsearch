package org.burnsearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.burnsearch.domain.Camp;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.dto.CampEtlDto;
import org.burnsearch.service.dto.EventEtlDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

/**
 * Tests that the ETL service can index a set of known documents.
 */
public class EtlServiceTest {

  @InjectMocks
  private EtlService etlService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private CampSearchRepository campSearchRepository;

  @Mock
  private EventSearchRepository eventSearchRepository;

  @Mock
  private ElasticsearchTemplate template;

  private String playaEventsCampUrl;
  private String playaEventsEventUrl;
  private CampEtlDto[] campDtos;
  private EventEtlDto[] eventDtos;
  private List<Event> expectedEvents;
  private List<Camp> expectedCamps;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    playaEventsCampUrl = "http://example.com/camps";
    playaEventsEventUrl = "http://example.com/event";
    ReflectionTestUtils.setField(etlService, "playaEventsCampUrl", playaEventsCampUrl);
    ReflectionTestUtils.setField(etlService, "playaEventsEventUrl", playaEventsEventUrl);
    ObjectMapper objectMapper = new ObjectMapper();
    List<CampEtlDto> campEtlDtoList = objectMapper.readValue(getJsonString("camps.json"),
        new TypeReference<List<CampEtlDto>>() {});
    campDtos = campEtlDtoList.toArray(new CampEtlDto[campEtlDtoList.size()]);
    List<EventEtlDto> eventEtlDtoList = objectMapper.readValue(getJsonString("events.json"),
        new TypeReference<List<EventEtlDto>>() {});
    eventDtos = eventEtlDtoList.toArray(new EventEtlDto[eventEtlDtoList.size()]);
    expectedCamps = getDocuments(campDtos, CampEtlDto::toCampDocument);
    expectedEvents = getDocuments(eventDtos, EventEtlDto::toEventDocument);
  }

  private String getJsonString(String jsonResource) throws IOException {
    return Resources.toString(Resources.getResource(jsonResource),
        Charset.forName("UTF-8"));
  }

  private <DTO, DOC> List<DOC> getDocuments(DTO[] dtos, Function<DTO, DOC> transform) {
    return Arrays.stream(dtos)
        .map(transform)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Test
  public void testIndex() throws Exception {
    when(restTemplate.getForObject(playaEventsEventUrl,
        EventEtlDto[].class)).thenReturn(eventDtos);
    when(restTemplate.getForObject(playaEventsCampUrl,
        CampEtlDto[].class)).thenReturn(campDtos);

    etlService.indexCampsAndEvents();


    class Matcher<T> extends ArgumentMatcher<List<IndexQuery>> {
      private final List<T> expectedEntities;

      Matcher(List<T> expectedEntities) {
        this.expectedEntities = expectedEntities;
      }

      @Override
      public boolean matches(Object o) {
        List<IndexQuery> indexQueries = (List<IndexQuery>) o;
        return expectedEntities.size() == indexQueries.size() &&
            expectedEntities.stream().allMatch(entity -> indexQueries.stream().anyMatch(query ->
                query.getObject().equals(entity)));
      }
    }
    verify(template, times(1)).bulkIndex(Matchers.<List<IndexQuery>>argThat(
        new Matcher(expectedCamps)));
    verify(template, times(1)).bulkIndex(Matchers.<List<IndexQuery>>argThat(
        new Matcher(expectedEvents)));
    verify(template, times(1)).refresh(Camp.class, true);
    verify(template, times(1)).delete(any(DeleteQuery.class), eq(Camp.class));
    verify(template, times(1)).delete(any(DeleteQuery.class), eq(Event.class));
  }
}
