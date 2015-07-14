package org.burnsearch.service;

import org.burnsearch.domain.Camp;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.dto.CampEtlDto;
import org.burnsearch.service.dto.EventEtlDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that loads information from the Burning Man API into an Elasticsearch index.
 * <p>
 * REPLACE ME WITH MORE DETAILS.
 */
@Service
public class EtlService {
  @Inject
  private RestTemplate restTemplate;

  @Inject
  private CampSearchRepository campSearchRepository;

  @Inject
  private EventSearchRepository eventSearchRepository;

  @Inject
  private ElasticsearchTemplate elasticsearchTemplate;

  @Value("${etl.playaEventsCamp}")
  private String playaEventsCampUrl;

  @Value("${etl.playaEventsEvent}")
  private String playaEventsEventUrl;

  // Executes once a day at 1 am.
  @Scheduled(cron = "0 0 1 * * ?")
  public void indexCampsAndEvents() throws IOException, ParseException {
    eventSearchRepository.deleteAll();
    campSearchRepository.deleteAll();
    indexEvents();
    indexCamps();
    elasticsearchTemplate.refresh(Camp.class, true);
  }

  private void indexEvents() throws IOException, ParseException {
    List<Event> events = fetchEvents();
    for (Event event : events) {
      eventSearchRepository.index(event);
    }
  }

  private void indexCamps() throws IOException, ParseException {
    List<Camp> camps = fetchCamps();
    for (Camp camp : camps) {
      campSearchRepository.index(camp);
    }
  }

  private List<Event> fetchEvents() throws IOException, ParseException {
    EventEtlDto[] events = restTemplate.getForObject(playaEventsEventUrl, EventEtlDto[].class);
    return Arrays.stream(events)
        .map(event -> event.toEventDocument())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private List<Camp> fetchCamps() throws IOException, ParseException {
    CampEtlDto[] camps = restTemplate.getForObject(playaEventsCampUrl, CampEtlDto[].class);
    return Arrays.stream(camps)
        .map(camp -> camp.toCampDocument())
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
