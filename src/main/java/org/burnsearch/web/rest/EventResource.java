package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.web.rest.dto.SearchResultsDTO;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Endpoints for accessing information about Burning Man events.
 */
@RestController
@RequestMapping("/api/events")
public class EventResource {

  @Inject
  private EventSearchRepository eventSearchRepository;

  @RequestMapping(value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public Event getEventById(@PathVariable("id") Long id) {
    return eventSearchRepository.findOne(id);
  }

  @RequestMapping(value = "/search/description",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public SearchResultsDTO<Event> searchByDescription(@RequestParam(value = "q") String description,
      @RequestParam(value = "page", defaultValue = "0") int pageNumber,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    QueryBuilder queryBuilder = new MatchQueryBuilder("description", description);
    FacetedPage<Event> searchResults = eventSearchRepository
        .search(queryBuilder, new PageRequest(pageNumber, size));
    return new SearchResultsDTO<>(searchResults.getNumber(),
        searchResults.getTotalElements(),
        searchResults.getContent());
  }
}
