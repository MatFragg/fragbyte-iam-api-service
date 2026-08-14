package com.fragbyte.iam.domain.model.valueobjects;

/**
 * Access roles for platform-level authorization.
 *
 * <p>Controls what a user can DO in the system (admin endpoints, support
 * tools, etc.).</p>
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public enum AccessRole {
  USER,
  ADMIN,
  SUPERADMIN,
  SUPPORT;

  /**
   * Generates the role as an authority.
   *
   * @return a string with the role as an authority
   */
  public String asAuthority() {
    return "ROLE_" + name();
  }
}
