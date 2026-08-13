package com.fragbyte.shared.domain.model.valueobjects;

import java.util.List;

/**
 * Framework-free pagination container used by domain service ports so the domain layer never
 * depends on Spring Data.
 *
 * @param content the page content
 * @param totalElements the total number of elements across all pages
 * @param page the zero-based page number
 * @param size the page size
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record Paged<T>(List<T> content, long totalElements, int page, int size) {

  /**
   * Creates a new {@link Paged} instance.
   *
   * @param content the page content
   * @param totalElements the total number of elements across all pages
   * @param page the zero-based page number
   * @param size the page size
   * @param <T> the element type
   * @return the paged container
   */
  public static <T> Paged<T> of(List<T> content, long totalElements, int page, int size) {
    return new Paged<>(content, totalElements, page, size);
  }
}
