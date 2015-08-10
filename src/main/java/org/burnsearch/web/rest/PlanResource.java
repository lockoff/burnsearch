package org.burnsearch.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.burnsearch.domain.Event;
import org.burnsearch.domain.Camp;
import org.burnsearch.repository.search.CampSearchRepository;
import org.burnsearch.repository.search.EventSearchRepository;
import org.burnsearch.service.UserService;
import org.burnsearch.web.rest.dto.EventCalendarDto;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            @RequestParam(value = "size", defaultValue = "2147483647") int size) {
        Set<Long> eventsListIds = userService.getEventList();
        IdsQueryBuilder queryBuilder = new IdsQueryBuilder();
        for (Long eventId : eventsListIds) {
            queryBuilder.addIds(eventId.toString());
        }
        FacetedPage<Event> searchResults = eventSearchRepository
                .search(queryBuilder, new PageRequest(pageNumber, size));
        List<Event> sortedByDate = searchResults.getContent()
                .stream()
                .sorted((event1, event2) -> {
                    Date event1StartTime = event1.getOccurrenceSet().get(0).getStartTime();
                    Date event2StartTime = event2.getOccurrenceSet().get(0).getStartTime();
                    return event1StartTime.compareTo(event2StartTime);
                }).collect(Collectors.toCollection(ArrayList::new));
        return new SearchResultsDTO<>(searchResults.getNumber(),
                searchResults.getTotalElements(),
                sortedByDate);
    }

    @RequestMapping(value = "/plan/events/docs/print",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EventCalendarDto> getEventsListPrint() {
        Set<Long> eventsListIds = userService.getEventList();
        IdsQueryBuilder queryBuilder = new IdsQueryBuilder();
        for (Long eventId : eventsListIds) {
            queryBuilder.addIds(eventId.toString());
        }
        Iterable<Event> searchResults = eventSearchRepository
                .search(queryBuilder);
        HashMap<Date, List<Event>> eventsByDay = StreamSupport.stream(searchResults.spliterator(), false)
                .flatMap(event -> {
                    return event.getOccurrenceSet().stream().map(eventOccurrence -> {
                        Event eventRepeat = new Event(event);
                        eventRepeat.setOccurrenceSet(Lists.newArrayList(eventOccurrence));
                        return eventRepeat;
                    });
                })
                .map(event -> {
                    HashMap<Date, List<Event>> dayMap = new HashMap<>();
                    dayMap.put(DateUtils.truncate(event.getOccurrenceSet().get(0).getStartTime(), Calendar.DAY_OF_MONTH), Lists.newArrayList(event));
                    return dayMap;
                })
                .reduce(new HashMap<>(), (dayMap1, dayMap2) -> {
                    dayMap2.forEach((day, events) -> {
                        events.addAll(dayMap1.getOrDefault(day, new ArrayList<>()));
                        dayMap1.put(day, events);
                    });
                    return dayMap1;
                });
        return eventsByDay
                .entrySet()
                .stream()
                .map(entry -> {
                    EventCalendarDto dto = new EventCalendarDto();
                    dto.setDay(entry.getKey());
                    entry.getValue().sort((event1, event2) -> {
                        return event1.getOccurrenceSet().get(0).getStartTime().compareTo(event2.getOccurrenceSet().get(0).getStartTime());
                    });
                    dto.setEvents(entry.getValue());
                    return dto;
                })
                .sorted((eventDto1, eventDto2) -> eventDto1.getDay().compareTo(eventDto2.getDay()))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    @RequestMapping(value = "/plan/events/contains/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public boolean eventPlanContains(@PathVariable("id") Long eventId) {
    return userService.getEventList().contains(eventId);
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

  @RequestMapping(value = "/plan/camps/contains/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public boolean campPlanContains(@PathVariable("id") Long campId) {
    return userService.getCampList().contains(campId);
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
                                                   @RequestParam(value = "size", defaultValue = "2147483647") int size) {

        IdsQueryBuilder qb = QueryBuilders.idsQuery();
        for(Long campId: this.getCamps()) {
            qb.addIds(campId.toString());
        }
        FacetedPage<Camp> searchResults = campSearchRepository.search(qb, new PageRequest(page, size));
        List<Camp> sortedByName = searchResults.getContent().stream().sorted((camp1, camp2) ->
                (camp1.getName().compareTo(camp2.getName())))
                .collect(Collectors.toCollection(ArrayList::new));
        return new SearchResultsDTO<>(searchResults.getNumber(),
                searchResults.getTotalElements(),
                sortedByName);
    }
}
