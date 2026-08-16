package com.fragbyte.iam.domain.services;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.queries.GetAllUsersQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByEmailQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.shared.domain.model.valueobjects.Paged;

import java.util.Optional;

/**
 * User query service contract for IAM User queries.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface UserQueryService {

  /**
   * Gets all the users from database, paginated.
   *
   * @param query the query to get all the users.
   * @return a {@link Paged} container of users.
   */
  Paged<User> handle(GetAllUsersQuery query);

  /**
   * Gets a user by id from database.
   *
   * @param query the query to get a user by id.
   * @return an {@link Optional} containing the matching user if one exists; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<User> handle(GetUserByIdQuery query);

  /**
   * Gets a user by email from database.
   *
   * @param query the query to get a user by email.
   * @return on {@link Optional} containing the matching user if one exists; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<User> handle(GetUserByEmailQuery query);
}
