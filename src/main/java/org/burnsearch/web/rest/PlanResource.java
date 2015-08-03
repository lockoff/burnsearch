package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Event;
import org.burnsearch.domain.Camp;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.UserService;
import org.burnsearch.web.rest.dto.SearchResultsDTO;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Set;

/**
 * Resource exposing endpoints for CRUD operations on a user's personal plans (i.e. lists of events,
 * camps, etc. they find interesting).
 */
@RestController
@RequestMapping("/api")
public class PlanResource {
    @Inject
    private UserService userService;

    @Inject
    private EventSearchRepository eventSearchRepository;

    @Inject
    private CampSearchRepository campSearchRepository;

    @RequestMapping(value = "/plan/events/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> addEvent(@PathVariable("id") Long eventId) {
        userService.addToEventList(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/plan/events/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long eventId) {
        userService.removeFromEventList(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/plan/events",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Long> getEvents() {
        return userService.getEventList();
    }

    @RequestMapping(value = "/plan/events/docs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public SearchResultsDTO<Event> getEventsListDocs(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Set<Long> eventsListIds = userService.getEventList();
        IdsQueryBuilder queryBuilder = new IdsQueryBuilder();
        for (Long eventId : eventsListIds) {
            queryBuilder.addIds(eventId.toString());
        }
        FacetedPage<Event> searchResults = eventSearchRepository
                .search(queryBuilder, new PageRequest(pageNumber, size));
        return new SearchResultsDTO<>(searchResults.getNumber(),
                searchResults.getTotalElements(),
                searchResults.getContent());
    }

    @RequestMapping(value = "/plan/camps/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> addCamp(@PathVariable("id") Long campId) {
        userService.addToCampList(campId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/plan/camps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Long> getCamps() {
        return userService.getCampList();
    }

    @RequestMapping(value = "/plan/camps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteCamp(@PathVariable("id") Long campId) {
        userService.removeFromCampList(campId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/plan/camps/docs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public SearchResultsDTO<Camp> getCampsListDocs(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {

        IdsQueryBuilder qb = QueryBuilders.idsQuery();
        for(Long campId: this.getCamps()) {
            qb.addIds(campId.toString());
        }
        FacetedPage<Camp> searchResults = campSearchRepository.search(qb, new PageRequest(page, size));
        return new SearchResultsDTO<>(searchResults.getNumber(),
                searchResults.getTotalElements(),
                searchResults.getContent());
    }
}
