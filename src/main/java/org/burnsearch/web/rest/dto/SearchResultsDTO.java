package org.burnsearch.web.rest.dto;

import java.util.List;

/**
 * Data transfer object for pageable search results.
 */
public class SearchResultsDTO<T> {
  private final int currentPage;
  private final int totalPages;
  private final List<T> content;


  public SearchResultsDTO(int currentPage, int totalPages, List<T> content) {
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.content = content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public List<T> getContent() {
    return content;
  }
}
