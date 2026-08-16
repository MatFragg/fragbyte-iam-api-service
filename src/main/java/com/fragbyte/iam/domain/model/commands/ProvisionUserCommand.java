package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;

import java.util.Set;

/**
 * Provision user command.
 *
 * <p>This class represents the command to create a user on behalf of the platform. Unlike {@link
 * SignUpCommand}, it is issued by an authenticated administrator and may grant explicit access
 * roles. It never results in an authentication token.
 *
 * @param email the email of the user.
 * @param password the initial password of the user.
 * @param accessRoles the roles to grant to the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ProvisionUserCommand(
    Email email, RawPassword password, Set<AccessRoles> accessRoles) {

  /**
   * The provision user command constructor.
   *
   * @throws IllegalArgumentException if the password is shorter than 8 characters or no roles are
   *     provided.
   */
  public ProvisionUserCommand {
    if (password == null || password.password() == null || password.password().isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
    if (password.password().length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
    if (accessRoles == null || accessRoles.isEmpty()) {
      throw new IllegalArgumentException("At least one access role must be provided");
    }
    accessRoles = Set.copyOf(accessRoles);
  }
}
