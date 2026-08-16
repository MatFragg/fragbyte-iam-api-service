package com.fragbyte.iam.domain.exceptions;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;

/**
 * Thrown when a requested access role is not among the seeded roles.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class RoleNotFoundException extends RuntimeException {

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param role the role name that could not be found
   */
  public RoleNotFoundException(AccessRoles role) {
    super("The access role " + role + " is not registered.");
  }

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param role the role name that could not be found
   */
  public RoleNotFoundException(String role) {
    super("The access role " + role + " is not registered.");
  }
}
