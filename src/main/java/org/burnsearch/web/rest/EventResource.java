package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.EventSearchRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Endpoints for accessing information about Burning Man events.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

  @Inject
  private EventSearchRepository eventSearchRepository;

  @RequestMapping(value = "/event/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Timed
  public Event getEventById(@PathVariable("id") Long id) {
    return eventSearchRepository.findOne(id);
  }
}
