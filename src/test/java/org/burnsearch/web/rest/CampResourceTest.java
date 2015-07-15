package org.burnsearch.web.rest;

import com.google.common.collect.Lists;
import org.burnsearch.domain.Camp;
import org.burnsearch.repository.search.CampSearchRepository;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;


import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CampResourceTest {

  @Mock
  private CampSearchRepository mockRepository;

  @InjectMocks
  private CampResource campResource;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetCampById() throws Exception {
    Camp expectedCamp = new Camp();
    expectedCamp.setId(1L);

    when(mockRepository.findOne(1L)).thenReturn(expectedCamp);

    Camp actualCamp = campResource.getCampById(1L);
    assertEquals("Endpoint returns wrong camp.", expectedCamp, actualCamp);
  }

  @Test
  public void testSearchByDescription() throws Exception {
    Camp camp0 = new Camp();
    camp0.setDescription("I am camp 0");
    Camp camp1 = new Camp();
    camp0.setDescription("I am camp 1");
    Camp camp2 = new Camp();
    camp0.setDescription("I am camp 2");

    List<Camp> expectedSearchResults = Lists.newArrayList(camp0, camp1, camp2);

    FacetedPage<Camp> page = new FacetedPageImpl<>(expectedSearchResults);
    QueryBuilder qb = QueryBuilders.matchQuery("description", "query");
    PageRequest pageRequest = new PageRequest(1, 3);

    ArgumentMatcher<QueryBuilder> queryMatcher = new ArgumentMatcher<QueryBuilder>() {
      @Override
      public boolean matches(Object o) {
        return o instanceof MatchQueryBuilder &&
            BytesReference.Helper.bytesEqual(qb.buildAsBytes(),
                ((MatchQueryBuilder) o).buildAsBytes());
      }
    };

    when(mockRepository.search(argThat(queryMatcher), eq(pageRequest))).thenReturn(page);

    List<Camp> actualSearchResults = campResource.searchByDescription("query", 1, 3);
    assertEquals("Endpoint returns wrong search results.", expectedSearchResults, actualSearchResults);
  }
}