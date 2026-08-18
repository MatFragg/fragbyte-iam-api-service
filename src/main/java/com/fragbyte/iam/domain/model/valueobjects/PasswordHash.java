package com.fragbyte.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Password hash value object.
 *
 * <p>Represents a one-way encoded password as an opaque string. The hashing algorithm selection and
 * verification are infrastructure concerns — the domain treats this as a dumb, opaque carrier.
 *
 * @param value the encoded password hash
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Embeddable
public record PasswordHash(@Column(name = "password_hash") String value) {

  /**
   * The value object constructor.
   *
   * @throws IllegalArgumentException if the hash is null or blank
   */
  public PasswordHash {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Password hash cannot be null or blank");
    }
  }
}
