package com.fragbyte.iam.domain.services;

import com.fragbyte.iam.domain.model.commands.SeedRolesCommand;

/**
 * Role command service contract for IAM access role commands.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface RoleCommandService {

  /**
   * Ensures the canonical platform access roles exist in the database.
   *
   * <p>Idempotent: roles that already exist are left untouched.
   *
   * @param command the seed roles command
   */
  void handle(SeedRolesCommand command);
}
