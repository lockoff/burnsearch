package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Camp;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.web.rest.dto.SearchResultsDTO;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Endpoints for accessing information about Burning Man camps.
 */
@RestController
@RequestMapping("/api/camps")
public class CampResource {

  @Inject
  private CampSearchRepository campSearchRepository;

  @RequestMapping(value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public Camp getCampById(@PathVariable("id") Long id) {
    return campSearchRepository.findOne(id);
  }

  @RequestMapping(value = "/search/description",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public SearchResultsDTO<Camp> searchByDescription(@RequestParam(value = "q") String query,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
    QueryBuilder qb = QueryBuilders.matchQuery("description", query);
    FacetedPage<Camp> searchResults = campSearchRepository.search(qb, new PageRequest(page, size));
    return new SearchResultsDTO<>(searchResults.getNumber(),
        searchResults.getTotalElements(),
        searchResults.getContent());
  }

  @RequestMapping(value = "/search",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public SearchResultsDTO<Camp> searchMultiMatch(@RequestParam(value = "q") String query,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    QueryBuilder qb = QueryBuilders.multiMatchQuery(query, "name", "description");
    FacetedPage<Camp> searchResults = campSearchRepository.search(qb, new PageRequest(page, size));
    return new SearchResultsDTO<>(searchResults.getNumber(),
        searchResults.getTotalElements(),
        searchResults.getContent());
  }

}
