package org.burnsearch.repository.search;

import org.burnsearch.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * A Spring data repository for event documents.
 */
public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {
}
