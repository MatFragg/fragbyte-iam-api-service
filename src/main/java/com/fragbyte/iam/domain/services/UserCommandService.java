package com.fragbyte.iam.domain.services;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.commands.AssignAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.ChangeEmailCommand;
import com.fragbyte.iam.domain.model.commands.ChangePasswordCommand;
import com.fragbyte.iam.domain.model.commands.DisableUserCommand;
import com.fragbyte.iam.domain.model.commands.EnableUserCommand;
import com.fragbyte.iam.domain.model.commands.LinkFederatedIdentityCommand;
import com.fragbyte.iam.domain.model.commands.LockUserCommand;
import com.fragbyte.iam.domain.model.commands.ProvisionUserCommand;
import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;
import com.fragbyte.iam.domain.model.commands.RemoveAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.SignInCommand;
import com.fragbyte.iam.domain.model.commands.SignInWithProviderCommand;
import com.fragbyte.iam.domain.model.commands.SignUpCommand;
import com.fragbyte.iam.domain.model.commands.UnlinkFederatedIdentityCommand;
import com.fragbyte.iam.domain.model.commands.UnlockUserCommand;
import com.fragbyte.iam.domain.model.commands.VerifyEmailCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

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
   * @return an {@link Optional} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the generated authentication token;
   *     otherwise, {@link Optional#empty()} if authentication fails
   */
  Optional<ImmutablePair<User, String>> handle(SignInCommand command);

  /**
   * Registers a new user through the self-service flow.
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

  /**
   * Provisions a new user on behalf of the platform.
   *
   * @param command the command containing the provisioning data
   * @return the newly provisioned {@link User} aggregate
   */
  User handle(ProvisionUserCommand command);

  /**
   * Changes a user's email address.
   *
   * @param command the command containing the user identifier and the new email
   */
  void handle(ChangeEmailCommand command);

  /**
   * Changes a user's password after verifying the current password.
   *
   * @param command the command containing the user identifier and the passwords
   */
  void handle(ChangePasswordCommand command);

  /**
   * Verifies a user's email address.
   *
   * @param command the command containing the user identifier
   */
  void handle(VerifyEmailCommand command);

  /**
   * Locks a user account.
   *
   * @param command the command containing the user identifier
   */
  void handle(LockUserCommand command);

  /**
   * Unlocks a user account.
   *
   * @param command the command containing the user identifier
   */
  void handle(UnlockUserCommand command);

  /**
   * Disables a user account.
   *
   * @param command the command containing the user identifier
   */
  void handle(DisableUserCommand command);

  /**
   * Re-enables a disabled user account.
   *
   * @param command the command containing the user identifier
   */
  void handle(EnableUserCommand command);

  /**
   * Grants an access role to a user.
   *
   * @param command the command containing the user identifier and the role to grant
   */
  void handle(AssignAccessRoleCommand command);

  /**
   * Revokes an access role from a user.
   *
   * @param command the command containing the user identifier and the role to revoke
   */
  void handle(RemoveAccessRoleCommand command);

  /**
   * Authenticates a user via an external identity provider.
   *
   * @param command the command containing the provider and token
   * @return an {@link Optional} containing an {@link ImmutablePair} where the left element is the
   *     authenticated {@link User} and the right element is the generated authentication token;
   *     otherwise, {@link Optional#empty()} if authentication fails
   */
  Optional<ImmutablePair<User, String>> handle(SignInWithProviderCommand command);

  /**
   * Links a federated identity to an existing user account.
   *
   * @param command the command containing the user identifier, provider, and token
   */
  void handle(LinkFederatedIdentityCommand command);

  /**
   * Unlinks a federated identity from a user account.
   *
   * @param command the command containing the user identifier and provider
   */
  void handle(UnlinkFederatedIdentityCommand command);
}
