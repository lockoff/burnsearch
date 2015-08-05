package org.burnsearch.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.burnsearch.Application;
import org.burnsearch.domain.Camp;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.dto.CampEtlDto;
import org.burnsearch.service.dto.EventEtlDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

  private <DOC, DOCREPO extends ElasticsearchRepository<DOC, Long>> void
      assertDocsInIndex(DOCREPO repo, List<DOC> expectedDocs, Function<DOC, Long> idGetter) {
    for (DOC expectedDoc : expectedDocs) {
      DOC actualDoc = repo.findOne(idGetter.apply(expectedDoc));
      assertNotNull("Expected document not in repo!", actualDoc);
      assertEquals("Document in index not as expected.", expectedDoc, actualDoc);
    }
  }

  @Test
  public void testIndex() throws Exception {
    when(restTemplate.getForObject(playaEventsEventUrl,
        EventEtlDto[].class)).thenReturn(eventDtos);
    when(restTemplate.getForObject(playaEventsCampUrl,
        CampEtlDto[].class)).thenReturn(campDtos);

    etlService.indexCampsAndEvents();

    verify(campSearchRepository, times(1)).deleteAll();
    verify(eventSearchRepository, times(1)).deleteAll();
    verify(template, times(1)).refresh(Camp.class, true);
    verify(template, times(2)).bulkIndex(anyList());
  }
}
