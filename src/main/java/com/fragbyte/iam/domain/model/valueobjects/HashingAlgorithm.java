package com.fragbyte.iam.domain.model.valueobjects;

/**
 * Password hashing algorithm used by a stored {@link PasswordHash}.
 *
 * <p>The domain names the algorithm <em>concept</em> so the model stays agnostic to any concrete
 * library or technology. Infrastructure adapters map their own implementation (e.g. BCrypt) onto
 * one of these values.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public enum HashingAlgorithm {
  BCRYPT,
  ARGON2;
}
