package org.burnsearch.repository.search;

import org.burnsearch.domain.Camp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * A Spring Data repository for camp documents.
 */
public interface CampSearchRepository extends ElasticsearchRepository<Camp, Long> {
}
