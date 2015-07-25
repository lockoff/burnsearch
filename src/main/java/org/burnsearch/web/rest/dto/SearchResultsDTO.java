package org.burnsearch.web.rest.dto;

import java.util.List;

/**
 * Data transfer object for pageable search results.
 */
public class SearchResultsDTO<T> {
  private final int currentPage;
  private final long totalItems;
  private final List<T> content;


  public SearchResultsDTO(int currentPage, long totalItems, List<T> content) {
    this.currentPage = currentPage;
    this.totalItems = totalItems;
    this.content = content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public long getTotalItems() {
    return totalItems;
  }

  public List<T> getContent() {
    return content;
  }
}
