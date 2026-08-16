package com.fragbyte.iam.domain.model.commands;

/**
 * Seed roles command.
 *
 * <p>This class represents the command to ensure the canonical platform access roles exist in the
 * database. It is idempotent: roles that already exist are left untouched.
 *
 * @see com.fragbyte.iam.domain.model.entities.AccessRole
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record SeedRolesCommand() {}
