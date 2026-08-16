package com.fragbyte.iam.domain.model.queries;

/**
 * Get all the users query.
 *
 * <p>This class represents the get all the users query, paginated to keep the result set bounded.
 *
 * @param page the zero-based page number.
 * @param size the page size.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record GetAllUsersQuery(int page, int size) {

  /** Default page size used when no page is requested. */
  public static final int DEFAULT_PAGE_SIZE = 10;

  /**
   * The get all users query constructor.
   *
   * @throws IllegalArgumentException if the page is negative or the size is not positive.
   */
  public GetAllUsersQuery {
    if (page < 0) {
      throw new IllegalArgumentException("Page must be zero or greater");
    }
    if (size <= 0) {
      throw new IllegalArgumentException("Size must be greater than zero");
    }
  }
}
