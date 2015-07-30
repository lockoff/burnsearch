package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.domain.Event;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.UserService;
import org.burnsearch.web.rest.dto.SearchResultsDTO;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
import java.util.List;
import java.util.Set;

/**
 *
 */
@RestController
@RequestMapping("/api")
public class ListResource {
    @Inject
    private UserService userService;

    @Inject
    private EventSearchRepository eventSearchRepository;

    @Inject
    private CampSearchRepository campSearchRepository;

    @RequestMapping(value = "/list/events/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> addEvent(@PathVariable("id") Long eventId) {
        userService.addToEventList(eventId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/list/events/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long eventId) {
        userService.removeFromEventList(eventId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/list/events",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Long> getEvents() {
        return userService.getEventList();
    }

    @RequestMapping(value = "/list/events/docs",
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

    @RequestMapping(value = "/list/camps/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> addCamp(@PathVariable("id") Long campId) {
        userService.addToCampList(campId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/list/camps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Set<Long> getCamps() {
        return userService.getCampList();
    }

    @RequestMapping(value = "/list/camps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteCamp(@PathVariable("id") Long campId) {
        userService.removeFromCampList(campId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
