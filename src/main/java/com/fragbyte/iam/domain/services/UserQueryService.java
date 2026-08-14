package com.fragbyte.iam.domain.services;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.queries.GetAllUsersQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByEmailQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.shared.domain.model.valueobjects.ApplicationError;
import com.fragbyte.shared.domain.model.valueobjects.Result;

import java.util.List;

/**
 * User query service contract for IAM User queries.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface UserQueryService {

  /**
   * Gets all the users from database.
   *
   * @param query the query to get all the users.
   * @return a {@link Result} containing the list of users.
   */
  Result<List<User>, ApplicationError> handle(GetAllUsersQuery query);

  /**
   * Gets a user by id from database.
   *
   * @param query the query to get a user by id.
   * @return a {@link Result} containing the matching user if one exists; otherwise, a {@link
   *     Result.Failure} with an {@link ApplicationError}
   */
  Result<User, ApplicationError> handle(GetUserByIdQuery query);

  /**
   * Gets a user by email from database.
   *
   * @param query the query to get a user by email.
   * @return a {@link Result} containing the matching user if one exists; otherwise, a {@link
   *     Result.Failure} with an {@link ApplicationError}
   */
  Result<User, ApplicationError> handle(GetUserByEmailQuery query);
}
