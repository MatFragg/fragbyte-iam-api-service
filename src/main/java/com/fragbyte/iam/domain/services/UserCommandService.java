package com.fragbyte.iam.domain.services;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;
import com.fragbyte.iam.domain.model.commands.SignInCommand;
import com.fragbyte.iam.domain.model.commands.SignUpCommand;
import com.fragbyte.shared.domain.model.valueobjects.ApplicationError;
import com.fragbyte.shared.domain.model.valueobjects.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * User command service contract for IAM User commands.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface UserCommandService {

  /**
   * Authenticates an existing user.
   *
   * @param command the command containing the user's credentials
   * @return a {@link Result} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the generated authentication token;
   *     otherwise, a {@link Result.Failure} with an {@link ApplicationError}
   */
  Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);

  /**
   * Registers a new user.
   *
   * @param command the command containing the registration data
   * @return a {@link Result} containing an {@link ImmutablePair} where the left element is the
   *     newly created {@link User} and the right element is the generated authentication token;
   *     otherwise, a {@link Result.Failure} with an {@link ApplicationError}
   */
  Result<ImmutablePair<User, String>, ApplicationError> handle(SignUpCommand command);

  /**
   * Refreshes an authenticated user's access token.
   *
   * @param command the command containing the refresh token information
   * @return a {@link Result} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the newly generated authentication
   *     token; otherwise, a {@link Result.Failure} with an {@link ApplicationError}
   */
  Result<ImmutablePair<User, String>, ApplicationError> handle(RefreshTokenCommand command);
}
