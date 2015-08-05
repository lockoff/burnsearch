package org.burnsearch.service;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.burnsearch.domain.Camp;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.dto.CampEtlDto;
import org.burnsearch.service.dto.EventEtlDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
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
 */
@Service
public class EtlService {
  private static final Logger LOG = LoggerFactory.getLogger(EtlService.class);

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

  // Execute when the application starts, then once every 24 hours.
  @Scheduled(initialDelayString = "${etl.initialDelay}", fixedRateString = "${etl.fixedRate}")
  public void indexCampsAndEvents() throws IOException, ParseException {
    LOG.info("Beginning ETL from Burning Man API.");
    eventSearchRepository.deleteAll();
    campSearchRepository.deleteAll();
    indexEvents();
    indexCamps();
    elasticsearchTemplate.refresh(Camp.class, true);
    LOG.info("Finished ETL from Burning Man API.");
  }

  private <T> void bulkIndex(List<T> entities, Function<T, Long> getId) {
    List<IndexQuery> indexQueries = entities
            .stream()
            .map(entity -> new IndexQueryBuilder().withId(getId.apply(entity).toString()).withObject(entity).build())
            .collect(Collectors.toList());
    elasticsearchTemplate.bulkIndex(indexQueries);
  }
  private void indexEvents() throws IOException, ParseException {
    List<Event> events = fetchEvents();
    bulkIndex(events, Event::getId);
  }

  private void indexCamps() throws IOException, ParseException {
    List<Camp> camps = fetchCamps();
    bulkIndex(camps, Camp::getId);
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
