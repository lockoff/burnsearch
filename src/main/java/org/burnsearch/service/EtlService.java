package org.burnsearch.service;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.apache.commons.io.IOUtils;
import org.burnsearch.domain.Camp;
import org.burnsearch.domain.Event;
import org.burnsearch.service.dto.CampEtlDto;
import org.burnsearch.service.dto.EventEtlDto;
import org.burnsearch.web.rest.SiteMapResource;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
  private ElasticsearchTemplate elasticsearchTemplate;

  @Value("${etl.playaEventsCamp}")
  private String playaEventsCampUrl;

  @Value("${etl.playaEventsEvent}")
  private String playaEventsEventUrl;

  @Value("${etl.unofficialMap}")
  private String unofficialMapUrl;

  // Execute when the application starts, then once every 24 hours.
  @Scheduled(initialDelayString = "${etl.initialDelay}", fixedRateString = "${etl.fixedRate}")
  public void indexCampsAndEvents() throws IOException, ParseException {
    LOG.info("Beginning ETL from Burning Man API.");
    Map<String, String> campNamesToLocations = fetchCampNamesToLocations();
    SiteMapResource.clearUrls();
    indexEntitiesDeleteStale(this::fetchEvents, Event::getId, SiteMapResource::addEventUrl, campNamesToLocations, Event.class);
    indexEntitiesDeleteStale(this::fetchCamps, Camp::getId, SiteMapResource::addCampUrl, campNamesToLocations, Camp.class);
    elasticsearchTemplate.refresh(Camp.class, true);
    LOG.info("Finished ETL from Burning Man API.");
  }

  private <T> void bulkIndex(List<T> entities, Function<T, Long> getId) {
    List<IndexQuery> indexQueries = entities
            .stream()
            .map(entity -> new IndexQueryBuilder().withId(getId.apply(entity).toString())
                .withObject(entity).build())
            .collect(Collectors.toList());
    elasticsearchTemplate.bulkIndex(indexQueries);
  }

  private <T> void deleteIfNotIds(List<String> ids, Class<T> entityClass) {
    IdsQueryBuilder idsQuery = new IdsQueryBuilder();
    idsQuery.addIds(ids.toArray(new String[ids.size()]));
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQueryBuilder.mustNot(idsQuery);
    DeleteQuery deleteQuery = new DeleteQuery();
    deleteQuery.setQuery(boolQueryBuilder);

    elasticsearchTemplate.delete(deleteQuery, entityClass);
  }

  private <T> void indexEntitiesDeleteStale(Function<Map<String, String>, List<T>> fetcher,
      Function<T, Long> getId,
      Consumer<Long> siteMapUpdate,
      Map<String, String> campNamesToLocations,
      Class<T> entityClass) {
    List<T> entities = fetcher.apply(campNamesToLocations);
    bulkIndex(entities, getId);
    updateSiteMap(entities, siteMapUpdate, getId);
    deleteIfNotIds(
        entities.stream().map(entity -> getId.apply(entity).toString()).collect(Collectors.toList
            ()),
        entityClass);
  }

  private <T> void updateSiteMap(List<T> entities, Consumer<Long> siteMapUpdate, Function<T, Long> getId) {
    entities.forEach(entity -> siteMapUpdate.accept(getId.apply(entity)));
  }

  private List<Event> fetchEvents(Map<String, String> campNamesToLocations) {
    EventEtlDto[] events = restTemplate.getForObject(playaEventsEventUrl, EventEtlDto[].class);
    return Arrays.stream(events)
        .map(eventDto -> {
          Event event = eventDto.toEventDocument();
          String eventCamp = event.getHostingCamp() != null ? event.getHostingCamp().getName().toLowerCase() : null;
          if (eventCamp != null && campNamesToLocations.containsKey(eventCamp)) {
            event.setUnofficialMapLocation(campNamesToLocations.get(eventCamp));
          }
          return event;
        })
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private List<Camp> fetchCamps(Map<String, String> campNamesToLocations) {
    CampEtlDto[] camps = restTemplate.getForObject(playaEventsCampUrl, CampEtlDto[].class);
    return Arrays.stream(camps)
        .map(campDto -> {
          Camp camp = campDto.toCampDocument();
          String campName = camp.getName().toLowerCase();
          if (campNamesToLocations.containsKey(campName)) {
            camp.setUnofficialMapLocation(campNamesToLocations.get(campName));
          }
          return camp;
        })
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private Map<String, String> fetchCampNamesToLocations() throws IOException {
    File temp = File.createTempFile("kml-download", ".kmz");
    byte[] kmzBytes = restTemplate.getForObject(unofficialMapUrl, byte[].class);
    if (kmzBytes.length == 0) {
      return new HashMap<>();
    }
    try (InputStream downloadStream = new ByteArrayInputStream(kmzBytes);
         OutputStream fileOutStream = new FileOutputStream(temp)) {
      IOUtils.copy(downloadStream, fileOutStream);
      Kml kml = Kml.unmarshalFromKmz(temp)[0];
      Document kmlDocument = (Document) kml.getFeature();
      Folder themeCamp = (Folder) kmlDocument
          .getFeature()
          .stream()
          .filter(feature -> feature.getName().equals("Theme Camps"))
          .findAny()
          .get();
      Map<String, String> campNamesToLocations = new HashMap<>();
      themeCamp.getFeature()
          .stream()
          .forEach(feature -> {
            ExtendedData extendedData = feature.getExtendedData();
            Data locationData = extendedData.getData().stream().filter(data -> data.getName().equals("Location")).findAny().get();
            String location = locationData.getValue().replace(", Black Rock City, NV", "").trim();
            campNamesToLocations.put(feature.getName().toLowerCase(), location);
          });
      return campNamesToLocations;
    }
  }
}
