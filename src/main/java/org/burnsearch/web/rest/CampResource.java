package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Camp;
import org.burnsearch.repository.search.CampSearchRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.springframework.data.domain.PageRequest;
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
  public List<Camp> searchByDescription(@RequestParam(value = "q") String query,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
    QueryBuilder qb = QueryBuilders.matchQuery("description", query);
    return campSearchRepository.search(qb, new PageRequest(page, size)).getContent();
  }
}
