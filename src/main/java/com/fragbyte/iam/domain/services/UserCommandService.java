package com.hampcoders.glottia.platform.api.iam.domain.services;

import com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User;
import com.hampcoders.glottia.platform.api.iam.domain.model.commands.RefreshTokenCommand;
import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignInCommand;
import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignUpCommand;
import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;

/** User command service contract for IAM User commands. */
public interface UserCommandService {

  /**
   * Authenticates an existing user.
   *
   * @param command the command containing the user's credentials
   * @return an {@link Optional} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the generated authentication token;
   *     otherwise, {@link Optional#empty()} if authentication fails
   */
  Optional<ImmutablePair<User, String>> handle(SignInCommand command);

  /**
   * Registers a new user.
   *
   * @param command the command containing the registration data
   * @return an {@link Optional} containing an {@link ImmutablePair} where the left element is the
   *     newly created {@link User} and the right element is the generated authentication token;
   *     otherwise, {@link Optional#empty()} if the user cannot be registered
   */
  Optional<ImmutablePair<User, String>> handle(SignUpCommand command);

  /**
   * Refreshes an authenticated user's access token.
   *
   * @param command the command containing the refresh token information
   * @return an {@link Optional} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the newly generated authentication
   *     token; otherwise, {@link Optional#empty()} if the refresh token is invalid
   */
  Optional<ImmutablePair<User, String>> handle(RefreshTokenCommand command);
}
