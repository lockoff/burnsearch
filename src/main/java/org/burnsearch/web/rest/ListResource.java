package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
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